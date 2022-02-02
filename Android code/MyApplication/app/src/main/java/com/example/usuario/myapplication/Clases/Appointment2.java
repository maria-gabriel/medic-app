package com.example.usuario.myapplication.Clases;

import java.io.Serializable;

    //Clase citas para los médicos
public class Appointment2 implements Serializable{
    int Id_Cita;
    String Paciente;
    String Email;
    String Contacto;
    String Sintomas;
    String Hora;
    String Fecha;
    String Estado;
    String Latitud;
    String Longitud;

    public Appointment2(int id_Cita, String paciente, String email, String contacto, String sintomas, String hora, String fecha, String estado, String latitud, String longitud, String referencias) {
        Id_Cita = id_Cita;
        Paciente = paciente;
        Email = email;
        Contacto = contacto;
        Sintomas = sintomas;
        Hora = hora;
        Fecha = fecha;
        Estado = estado;
        Latitud = latitud;
        Longitud = longitud;
        Referencias = referencias;
    }

    String Referencias;



    public Appointment2(){

    }



    public int getId_Cita() {
        return Id_Cita;
    }

    public void setId_Cita(int id_Cita) {
        Id_Cita = id_Cita;
    }

    public String getPaciente() {
        return Paciente;
    }

    public void setPaciente(String paciente) {
        Paciente = paciente;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContacto() {
        return Contacto;
    }

    public void setContacto(String contacto) {
        Contacto = contacto;
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

    public String getReferencias() {
        return Referencias;
    }

    public void setReferencias(String referencias) {
        Referencias = referencias;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }


}
