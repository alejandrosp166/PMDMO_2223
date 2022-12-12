package iesmm.pmdm.pmdm_t4_listadinamica;

public class FutbolistaModelo {
    private String nombre, nacionalidad;
    private int imgCantante;

    public FutbolistaModelo() {

    }

    public FutbolistaModelo(String nombre, String nacionalidad, int imgCantante) {
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
        this.imgCantante = imgCantante;
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

    public int getImgCantante() {
        return imgCantante;
    }

    public void setImgCantante(int imgCantante) {
        this.imgCantante = imgCantante;
    }
}
