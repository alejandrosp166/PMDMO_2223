package iesmm.pmdm.tres_en_raya;

import java.util.Arrays;
import java.util.Random;

public class JuegoTresEnRaya {
    // Número de casillas del tablero
    public static final int DIM_TABLERO = 9;

    // Constantes usadas para representar JUGADOR, MÁQUINA, EN BLANCO
    public static final char JUGADOR = 'X';
    public static final char MAQUINA = 'O';
    public static final char BLANCO = ' ';

    // Número aleatorio generado para calcular MAQUINA
    private final Random aleatorio;

    // Estructura de dato del tablero
    private char[] tablero;

    public JuegoTresEnRaya() {
        tablero = new char[DIM_TABLERO];
        aleatorio = new Random();
        limpiarTablero();
    }

    public char[] getTablero() {
        return tablero;
    }

    public void setTablero(char[] tablero) {
        this.tablero = tablero;
    }

    /**
     * Limpia el tablero de todas las X y O
     */
    public void limpiarTablero() {
        // Resetea todos las casillas
        for (int i = 0; i < DIM_TABLERO; i++)
            tablero[i] = BLANCO;
    }

    /**
     * Pone una ficha del tipo en la casilla correspondiente del tablero. La
     * casilla debe estar en BLANCO y dentro de los límites del tablero.
     *
     * @param ficha   - JUGADOR ó MÁQUINA
     * @param casilla - La ubicación de la casilla del tablero (0-[DIM_TABLERO-1])
     *                que se pondrá ficha
     * @return TRUE si el movimiento fue realizado, FALSE en caso contrario.
     */
    public boolean moverFicha(char ficha, int casilla) {
        boolean casilla_ocupada = false;

        if (casilla >= 0 && casilla < DIM_TABLERO && tablero[casilla] == BLANCO) {
            tablero[casilla] = ficha;
            casilla_ocupada = true;
        }

        return casilla_ocupada;
    }

    /**
     * Comprueba si existe un ganador. Devuelve un valor del estado del tablero
     * existente (tipo de ganador, no ganador ó empate), PRIORIZAMOS AL JUGADOR
     *
     * @return Devuelve : * 0 si NO hay ganador * 1 si JUGADOR gana * 0 si HAY EMPATE
     */
    public int comprobarGanador() {
        // Determina si ha encontrado un valor de estado del tablero
        boolean encontrado = false;
        int valor = -1; // Valor del estado resultante
        int i; // Índice para recorrer el tablero

        // COMPROBACIÓN: LÍNEA HORIZONTAL (FILA)
        i = 0;
        while (i <= 6 && !encontrado) {
            if (tablero[i] == JUGADOR && tablero[i + 1] == JUGADOR
                    && tablero[i + 2] == JUGADOR) {
                valor = 1;
                encontrado = true;
            } else if (tablero[i] == MAQUINA && tablero[i + 1] == MAQUINA
                    && tablero[i + 2] == MAQUINA) {
                valor = 2;
                encontrado = true;
            }

            i += 3;
        }

        // COMPROBACIÓN: LÍNEA VERTICAL (COLUMNA)
        i = 0;
        while (i <= 2 && !encontrado) {
            if (tablero[i] == JUGADOR && tablero[i + 3] == JUGADOR
                    && tablero[i + 6] == JUGADOR) {
                valor = 1;
                encontrado = true;
            } else if (tablero[i] == MAQUINA && tablero[i + 3] == MAQUINA
                    && tablero[i + 6] == MAQUINA) {
                valor = 2;
                encontrado = true;
            }

            i++;
        }

        // COMPROBACIÓN: LÍNEA DIAGONAL (DIAGONAL)
        if ((tablero[0] == JUGADOR && tablero[4] == JUGADOR && tablero[8] == JUGADOR)
                || (tablero[2] == JUGADOR && tablero[4] == JUGADOR && tablero[6] == JUGADOR)) {
            valor = 1;
            encontrado = true;
        } else if ((tablero[0] == MAQUINA && tablero[4] == MAQUINA && tablero[8] == MAQUINA)
                || (tablero[2] == MAQUINA && tablero[4] == MAQUINA && tablero[6] == MAQUINA)) {
            valor = 2;
            encontrado = true;
        }

        // Comprueba NO ganador
        i = 0;
        while (i < DIM_TABLERO && !encontrado) {
            if (tablero[i] == BLANCO) {
                valor = -1;
                encontrado = true;
            }

            i++;
        }

        // EMPATE
        if (!encontrado)
            valor = 0;

        return valor;
    }

    /**
     * Devuelve una casilla para que mueva ficha la MAQUINA.
     *
     * @return Un movimiento dependiendo del nivel de dificultad elegido.
     */
    public int getMovimientoMaquina(int dificultad) {
        // NIVEL DE DIFICULTAD 1: MOVIMIENTO ALEATORIO
        int casilla = 0;
        switch (dificultad) {
            case 0:
                casilla = facil();
                break;
            case 1:
                casilla = medio();
                break;
        }
        return casilla;
    }

    /**
     * Devuelve una casilla aleatoria entre las casillas vacías
     *
     * @return Un movimiento aleatorio
     */
    private int facil() {
        // Generación de una casilla aleatoria
        int casilla;

        do {
            casilla = aleatorio.nextInt(9);
        } while (tablero[casilla] == JUGADOR || tablero[casilla] == MAQUINA);

        return casilla;
    }

    /**
     * Devuelve una casilla dependiendo de unas reglas establecidas.
     *
     * 1 -> Si la máquina puede ganar que gane.
     * 2 -> Si no puede ganar y puede evitar al usuario que gane.
     * 3 -> Si no puede ganar, ni evitar al usuario que gane, pone la ficha en medio.
     * 4 -> Si no puede hacer ninguna de las 3 anteriores utiliza el método facil().
     *
     * NOTA -> No se ha implementado en la línea vertical para que sea un nivel medio de dificultad.
     *
     * @return Un movimiento válido dependiendo de la situación del tablero en
     * ese momento y teniendo en cuenta las regla establecidas.
     */
    private int medio() {
        // Si puede ganar que gane
        // Comprueba todas las posibilidades en el plano horizontal (O-O-* || 0-*-0 || *-0-0)
        for (int i = 0; i <= 6; i += 3) {
            if (tablero[i] == MAQUINA && tablero[i + 1] == MAQUINA && tablero[i + 2] == BLANCO) {
                return i + 2;
            } else if (tablero[i] == MAQUINA && tablero[i + 1] == BLANCO && tablero[i + 2] == MAQUINA) {
                return i + 1;
            } else if (tablero[i] == BLANCO && tablero[i + 1] == MAQUINA && tablero[i + 2] == MAQUINA) {
                return i;
            }
        }
        // Vertical
        // Comprueba todas las posibilidades en el plano vertical (O-O-* || 0-*-0 || *-0-0)
        for (int i = 0; i <= 2; i++) {
            if (tablero[i] == MAQUINA && tablero[i + 3] == MAQUINA && tablero[i + 6] == BLANCO) {
                return i + 6;
            } else if (tablero[i] == MAQUINA && tablero[i + 3] == BLANCO && tablero[i + 6] == MAQUINA) {
                return i + 3;
            } else if (tablero[i] == BLANCO && tablero[i + 3] == MAQUINA && tablero[i + 6] == MAQUINA) {
                return i;
            }
        }
        // Diagonal
        // Comprueba todas las posibilidades en el plano diagonal, para ganar
        if (tablero[0] == MAQUINA && tablero[4] == MAQUINA && tablero[8] == BLANCO){
            return 8;
        } else if (tablero[0] == MAQUINA && tablero[4] == BLANCO && tablero[8] == MAQUINA){
            return 4;
        } else if (tablero[0] == BLANCO && tablero[4] == MAQUINA && tablero[8] == MAQUINA){
            return 0;
        }

        if(tablero[2] == MAQUINA && tablero[4] == MAQUINA && tablero[6] == BLANCO){
            return 6;
        } else if (tablero[2] == MAQUINA && tablero[4] == BLANCO && tablero[6] == MAQUINA){
            return 4;
        } else if (tablero[2] == BLANCO && tablero[4] == MAQUINA && tablero[6] == MAQUINA){
            return 2;
        }

        // Si no puede ganar que bloquee al jugador
        // Comprueba todas las posibilidades en el plano horizontal (X-X-* || X-*-X || *-X-X)
        for (int i = 0; i <= 6; i += 3) {
            if (tablero[i] == JUGADOR && tablero[i + 1] == JUGADOR && tablero[i + 2] == BLANCO) {
                return i + 2;
            } else if (tablero[i] == JUGADOR && tablero[i + 1] == BLANCO && tablero[i + 2] == JUGADOR) {
                return i + 1;
            } else if (tablero[i] == BLANCO && tablero[i + 1] == JUGADOR && tablero[i + 2] == JUGADOR) {
                return i;
            }
        }
        // Vertical
        // Comprueba todas las posibilidades en el plano vertical (X-X-* || X-*-X || *-X-X)
        for (int i = 0; i <= 2; i++) {
            if (tablero[i] == JUGADOR && tablero[i + 3] == JUGADOR && tablero[i + 6] == BLANCO) {
                return i + 6;
            } else if (tablero[i] == JUGADOR && tablero[i + 3] == BLANCO && tablero[i + 6] == JUGADOR) {
                return i + 3;
            } else if (tablero[i] == BLANCO && tablero[i + 3] == JUGADOR && tablero[i + 6] == JUGADOR) {
                return i;
            }
        }
        // Diagonal
        // Comprueba todas las posibilidadesen plano diagonal
        if (tablero[0] == JUGADOR && tablero[4] == JUGADOR && tablero[8] == BLANCO){
            return 8;
        } else if (tablero[0] == JUGADOR && tablero[4] == BLANCO && tablero[8] == JUGADOR){
            return 4;
        } else if (tablero[0] == BLANCO && tablero[4] == JUGADOR && tablero[8] == JUGADOR){
            return 0;
        }

        if(tablero[2] == JUGADOR && tablero[4] == JUGADOR && tablero[6] == BLANCO){
            return 6;
        } else if (tablero[2] == JUGADOR && tablero[4] == BLANCO && tablero[6] == JUGADOR){
            return 4;
        } else if (tablero[2] == BLANCO && tablero[4] == JUGADOR && tablero[6] == JUGADOR){
            return 2;
        }

        // Si no puede ganar ni bloquear al jugador  pon la ficha en medio del tablero
        if (tablero[4] == BLANCO) {
            return 4;
        }
        // Si no puede poner la ficha en medio, coloca la ficha de manera aleatoria
        return facil();
    }
}