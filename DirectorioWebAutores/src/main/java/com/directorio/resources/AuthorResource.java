package com.directorio.resources;
import com.directorio.model.Author;
import com.directorio.model.AuthorModel;
import com.directorio.model.LiteraryGenre;
import com.directorio.model.LiteraryGenreModel;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
/**
 *
 * @author gris_
 */

@Path("autores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private final AuthorModel authorModel = new AuthorModel();
    private final LiteraryGenreModel genreModel = new LiteraryGenreModel();

    
    @GET
    public Response getAll(
            @QueryParam("genreId") Integer genreId,
            @QueryParam("nombre")  String nombre) {

        List<Author> lista;

        if (genreId != null && genreId != 0) {
            lista = authorModel.findByGenre(genreId);
        } else if (nombre != null && !nombre.trim().isEmpty()) {
            lista = authorModel.findByName(nombre.trim());
        } else {
            lista = authorModel.findAll();
        }

        return Response.ok(toJsonArray(lista)).build();
    }

    // GET /resources/autores/{id}
    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") Integer id) {
        Author author = findAuthorById(id);
        if (author == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Autor no encontrado\"}")
                    .build();
        }
        return Response.ok(toJson(author)).build();
    }
 
    // Retorna todo generos literarios

    @GET
    @Path("generos")
    public Response getGeneros() {
        List<LiteraryGenre> generos = genreModel.findAllGenres();
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < generos.size(); i++) {
            LiteraryGenre g = generos.get(i);
            sb.append("{")
              .append("\"id\":").append(g.getId()).append(",")
              .append("\"name\":\"").append(escape(g.getName())).append("\"")
              .append("}");
            if (i < generos.size() - 1) sb.append(",");
        }
        sb.append("]");
        return Response.ok(sb.toString()).build();
    }

  
    @POST
    public Response create(String body) {
        try {
            Author author = parseAuthorJson(body);
            if (author.getName() == null || author.getName().trim().isEmpty()) {
                return badRequest("El campo 'name' es obligatorio");
            }
            if (author.getBirthDate() == null) {
                return badRequest("El campo 'birthDate' (formato yyyy-MM-dd) es obligatorio");
            }

            // Validar y asignar género si se envió
            if (author.getGenre() != null && author.getGenre().getId() != null) {
                LiteraryGenre genre = genreModel.findById(author.getGenre().getId());
                if (genre == null) {
                    return badRequest("Género con id " + author.getGenre().getId() + " no existe");
                }
                author.setGenre(genre);
            }

            authorModel.create(author);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\":\"Autor creado exitosamente\"}")
                    .build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + escape(e.getMessage()) + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Integer id, String body) {
        try {
            Author existing = findAuthorById(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Autor no encontrado\"}")
                        .build();
            }

            Author updated = parseAuthorJson(body);
            updated.setId(id);

            if (updated.getName() == null || updated.getName().trim().isEmpty()) {
                return badRequest("El campo 'name' es obligatorio");
            }

            // Valida y asignra genero
            if (updated.getGenre() != null && updated.getGenre().getId() != null) {
                LiteraryGenre genre = genreModel.findById(updated.getGenre().getId());
                if (genre == null) {
                    return badRequest("Género con id " + updated.getGenre().getId() + " no existe");
                }
                updated.setGenre(genre);
            }

            authorModel.update(updated);
            return Response.ok("{\"mensaje\":\"Autor actualizado exitosamente\"}").build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + escape(e.getMessage()) + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Integer id) {
        Author existing = findAuthorById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Autor no encontrado\"}")
                    .build();
        }
        authorModel.delete(id);
        return Response.ok("{\"mensaje\":\"Autor eliminado exitosamente\"}").build();
    }

   
    // MÉTODOS AUXILIARES PRIVADOS

    /** Busca un Author por el id */
    private Author findAuthorById(Integer id) {
        return authorModel.findAll().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private String toJson(Author a) {
        String genreJson = "null";
        if (a.getGenre() != null) {
            genreJson = "{"
                + "\"id\":" + a.getGenre().getId() + ","
                + "\"name\":\"" + escape(a.getGenre().getName()) + "\""
                + "}";
        }

        String birthDateStr = "null";
        if (a.getBirthDate() != null) {
            birthDateStr = "\"" + new SimpleDateFormat("yyyy-MM-dd").format(a.getBirthDate()) + "\"";
        }

        return "{"
            + "\"id\":"        + a.getId()             + ","
            + "\"name\":\""    + escape(a.getName())   + "\","
            + "\"birthDate\":" + birthDateStr           + ","
            + "\"phone\":\""   + escape(nullSafe(a.getPhone())) + "\","
            + "\"genre\":"     + genreJson
            + "}";
    }

    /** conviente lista de author en jsonarray */
    private String toJsonArray(List<Author> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            sb.append(toJson(lista.get(i)));
            if (i < lista.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    private Author parseAuthorJson(String json) throws ParseException {
        Author author = new Author();
        author.setGenre(new LiteraryGenre());

        author.setName(extractString(json, "name"));
        author.setPhone(extractString(json, "phone"));

        String dateStr = extractString(json, "birthDate");
        if (dateStr != null && !dateStr.isEmpty()) {
            author.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").parse(dateStr));
        }

        // Extraer genre.id
        int genreStart = json.indexOf("\"genre\"");
        if (genreStart >= 0) {
            int braceOpen = json.indexOf("{", genreStart);
            int braceClose = json.indexOf("}", braceOpen);
            if (braceOpen >= 0 && braceClose >= 0) {
                String genreBlock = json.substring(braceOpen, braceClose + 1);
                String idStr = extractString(genreBlock, "id");
                if (idStr == null) {
                    int idIdx = genreBlock.indexOf("\"id\"");
                    if (idIdx >= 0) {
                        int colon = genreBlock.indexOf(":", idIdx);
                        int end = genreBlock.indexOf(",", colon);
                        if (end < 0) end = genreBlock.indexOf("}", colon);
                        idStr = genreBlock.substring(colon + 1, end).trim();
                    }
                }
                if (idStr != null && !idStr.isEmpty()) {
                    try {
                        author.getGenre().setId(Integer.parseInt(idStr.replace("\"", "").trim()));
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        return author;
    }

    /** Extrae el valor de campo string del JSON*/
    private String extractString(String json, String field) {
        String key = "\"" + field + "\"";
        int idx = json.indexOf(key);
        if (idx < 0) return null;
        int colon = json.indexOf(":", idx + key.length());
        if (colon < 0) return null;
        // salta espacios
        int start = colon + 1;
        while (start < json.length() && json.charAt(start) == ' ') start++;
        if (start >= json.length()) return null;
        if (json.charAt(start) == '"') {
            int end = json.indexOf('"', start + 1);
            if (end < 0) return null;
            return json.substring(start + 1, end);
        }
        // valor null o número
        int end = json.indexOf(',', start);
        if (end < 0) end = json.indexOf('}', start);
        if (end < 0) return null;
        String val = json.substring(start, end).trim();
        return val.equals("null") ? null : val;
    }

    /** caracteres especiales para json */
    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String nullSafe(String s) {
        return s == null ? "" : s;
    }

    private Response badRequest(String msg) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + escape(msg) + "\"}")
                .build();
    }
}