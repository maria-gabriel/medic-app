package com.example.usuario.myapplication.Clases;

import java.io.Serializable;

    //Clase citas para los pacientes
public class Appointment implements Serializable{
    int Id_Cita;
    String Sintomas;
    String Hora;
    String Fecha;
    String Estado;

    public Appointment(int id_Cita, String sintomas, String hora, String fecha, String estado) {
        Id_Cita = id_Cita;
        Sintomas = sintomas;
        Hora = hora;
        Fecha = fecha;
        Estado = estado;
    }

    public Appointment(){

    }

    public int getId_Cita() {
        return Id_Cita;
    }

    public void setId_Cita(int id_Cita) {
        Id_Cita = id_Cita;
    }

    public String getSintomas() {
        return Sintomas;
    }

    public void setSintomas(String sintomas) {
        Sintomas = sintomas;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

}
