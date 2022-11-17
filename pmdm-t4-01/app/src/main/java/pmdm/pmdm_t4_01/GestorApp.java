package pmdm.pmdm_t4_01;

public interface GestorApp {
    public void cargarNavegadorWeb(String url);
    public void abrirMarcadorLlamada();
    public void marcarLlamada(String tlfn);
    public void realizarLlamada(String tlfn);
    public void mandarSMS(String contenido);
    public void mandarSMS(String contenido, String tlfn);
}
