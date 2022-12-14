package pmdm.t4.pmdm_t4_listadinamica;

public class FutbolistaModelo {
    private String nombre;
    private String nacionalidad;
    private int imagen;

    public FutbolistaModelo() {

    }

    public FutbolistaModelo(String nombre, String nacionalidad, int imagen) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
