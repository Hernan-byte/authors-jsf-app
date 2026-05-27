/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
var AutoresAjax = (function () {

    // Detectar el context-root automáticamente.
    //   http://localhost:8080/DirectorioWebAutores/resources/autores
    var contextRoot = window.location.pathname.split("/")[1]
                      ? "/" + window.location.pathname.split("/")[1]
                      : "";
    var BASE_URL = window.location.origin + contextRoot + "/resources/autores";

   
    // Función interna genérica de petición AJAX
    function request(method, url, body, onSuccess, onError) {
        var xhr = new XMLHttpRequest();
        xhr.open(method, url, true);
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
        xhr.setRequestHeader("Accept",       "application/json");

        xhr.onreadystatechange = function () {
            if (xhr.readyState !== 4) return;

            var data = null;
            try { data = JSON.parse(xhr.responseText); } catch (e) { data = xhr.responseText; }

            if (xhr.status >= 200 && xhr.status < 300) {
                if (typeof onSuccess === "function") onSuccess(data);
            } else {
                var msg = (data && data.error) ? data.error
                        : "Error HTTP " + xhr.status;
                if (typeof onError === "function") onError(msg);
                else console.error("[AutoresAjax] " + msg);
            }
        };

        xhr.onerror = function () {
            var msg = "Error de red al conectar con " + url;
            if (typeof onError === "function") onError(msg);
            else console.error("[AutoresAjax] " + msg);
        };

        xhr.send(body ? JSON.stringify(body) : null);
    }

   
    // API pública
    function listarTodos(onSuccess, onError) {
        request("GET", BASE_URL, null, onSuccess, onError);
    }

 
    function filtrarPorGenero(genreId, onSuccess, onError) {
        request("GET", BASE_URL + "?genreId=" + encodeURIComponent(genreId),
                null, onSuccess, onError);
    }

 
    function filtrarPorNombre(nombre, onSuccess, onError) {
        request("GET", BASE_URL + "?nombre=" + encodeURIComponent(nombre),
                null, onSuccess, onError);
    }

    function obtenerPorId(id, onSuccess, onError) {
        request("GET", BASE_URL + "/" + id, null, onSuccess, onError);
    }

    
    function listarGeneros(onSuccess, onError) {
        request("GET", BASE_URL + "/literarios", null, onSuccess, onError);
    }

   
    function crear(autor, onSuccess, onError) {
        request("POST", BASE_URL, autor, onSuccess, onError);
    }

    
    function actualizar(id, autor, onSuccess, onError) {
        request("PUT", BASE_URL + "/" + id, autor, onSuccess, onError);
    }

    
    function eliminar(id, onSuccess, onError) {
        request("DELETE", BASE_URL + "/" + id, null, onSuccess, onError);
    }

   
    return {
        listarTodos:      listarTodos,
        filtrarPorGenero: filtrarPorGenero,
        filtrarPorNombre: filtrarPorNombre,
        obtenerPorId:     obtenerPorId,
        listarGeneros:    listarGeneros,
        crear:            crear,
        actualizar:       actualizar,
        eliminar:         eliminar
    };

})();