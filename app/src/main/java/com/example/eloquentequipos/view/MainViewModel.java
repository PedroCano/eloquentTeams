package com.example.eloquentequipos.view;


import android.app.Application;

import com.example.eloquentequipos.model.data.Equipo;
import com.example.eloquentequipos.model.data.Jugador;
import com.example.eloquentequipos.model.Repository;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class MainViewModel extends AndroidViewModel {

    private Repository repository;

    public MainViewModel(@NonNull Application application){

        super(application);
        repository= new Repository();

    }

    public LiveData<Equipo> equipoLiveData (long id){
        return repository.equipoLiveData(id);
    }

    public LiveData<Jugador> jugadoresLiveData (long id){
        return repository.jugadoresLiveData(id);
    }

    public LiveData<List<Equipo> >getLiveEquipoList(){
        return repository.getLiveEquipoList();
    }

    public void addEquipo(Equipo equipo) {
        repository.add(equipo);
    }

    public void deleteEquipo(long id) {
        repository.deleteEquipo(id);
    }

    public void deleteJugador(long id) {
        repository.deleteJugador(id);
    }

    public void updateEquipo(long id, Equipo equipo){
        repository.updateEquipo(id, equipo);
    }

    public void updateJugadores(long id, Jugador jugador){
        repository.updateJugador(id, jugador);
    }

    public LiveData<List<Jugador> >getLiveJugadoresList(){
        return repository.getLiveJugadoresList();
    }

    public void addJugadores(Jugador jugador) {
        repository.add(jugador);
    }

    public void upload(File file) {
        repository.upload(file);
    }
}
