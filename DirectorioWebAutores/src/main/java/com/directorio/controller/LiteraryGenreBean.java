package com.directorio.controller;

import com.directorio.model.LiteraryGenre;
import com.directorio.model.LiteraryGenreModel;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "literaryGenreBean")
@ApplicationScoped
public class LiteraryGenreBean implements Serializable {

    private LiteraryGenreModel genreModel = new LiteraryGenreModel();

    public List<LiteraryGenre> getListaGeneros() {
        return genreModel.findAllGenres();
    }
}
