package pmdm.pmdm_t4_agenda;

public class Contacto {
    String nombre;
    long numero;
    String email;

    public Contacto() {

    }

    public Contacto(String nombre, long numero, String email) {
        this.nombre = nombre;
        this.numero = numero;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
