package com.directorio.controller;

import com.directorio.model.Author;
import com.directorio.model.AuthorModel;
import com.directorio.model.LiteraryGenre;
import com.directorio.model.LiteraryGenreModel;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "authorBean")
@ViewScoped
public class AuthorBean implements Serializable {

    private Author author = new Author();
    private List<Author> listaAutores;
    private Integer filtroGenero;
    private String filtroNombre; // Para el h:inputText de búsqueda (punto 6c)

    private AuthorModel authorModel = new AuthorModel();
    private LiteraryGenreModel genreModel = new LiteraryGenreModel();

    // -------------------------------------------------------
    // Cargar lista según filtros activos
    // -------------------------------------------------------
    public List<Author> getListaAutores() {
        if (filtroGenero != null && filtroGenero != 0) {
            listaAutores = authorModel.findByGenre(filtroGenero);
        } else if (filtroNombre != null && !filtroNombre.trim().isEmpty()) {
            listaAutores = authorModel.findByName(filtroNombre.trim());
        } else {
            listaAutores = authorModel.findAll();
        }
        return listaAutores;
    }

    // -------------------------------------------------------
    // Guardar (Agregar o Actualizar)
    // -------------------------------------------------------
    public void guardar() {
        FacesContext ctx = FacesContext.getCurrentInstance();

        // Asignar el objeto LiteraryGenre completo desde el id seleccionado
        if (author.getGenre() != null && author.getGenre().getId() != null) {
            LiteraryGenre genre = genreModel.findById(author.getGenre().getId());
            author.setGenre(genre);
        }

        if (author.getId() == null) {
            // Verificar si ya existe un autor con el mismo nombre
            boolean existe = authorModel.findAll().stream()
                    .anyMatch(a -> a.getName().equalsIgnoreCase(author.getName()));
            if (existe) {
                ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Advertencia", "Este autor ya fue agregado anteriormente."));
            }
            authorModel.create(author);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Autor agregado correctamente."));
        } else {
            authorModel.update(author);
            ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Autor actualizado correctamente."));
        }

        // Limpiar formulario
        author = new Author();
        author.setGenre(new LiteraryGenre());
        listaAutores = null;
    }

    // -------------------------------------------------------
    // Editar: cargar datos del autor seleccionado al formulario
    // -------------------------------------------------------
    public void editar(Author a) {
        this.author = a;
    }

    // -------------------------------------------------------
    // Eliminar
    // -------------------------------------------------------
    public void eliminar(Integer id) {
        authorModel.delete(id);
        listaAutores = null;
    }

    // -------------------------------------------------------
    // Getters y Setters
    // -------------------------------------------------------
    public Author getAuthor() {
        if (author.getGenre() == null) {
            author.setGenre(new LiteraryGenre());
        }
        return author;
    }

    public void setAuthor(Author author) { this.author = author; }

    public Integer getFiltroGenero() { return filtroGenero; }

    public void setFiltroGenero(Integer filtroGenero) {
        this.filtroGenero = filtroGenero;
        this.listaAutores = null; // Forzar recarga al cambiar filtro
    }

    public String getFiltroNombre() { return filtroNombre; }

    public void setFiltroNombre(String filtroNombre) {
        this.filtroNombre = filtroNombre;
        this.listaAutores = null;
    }
}
