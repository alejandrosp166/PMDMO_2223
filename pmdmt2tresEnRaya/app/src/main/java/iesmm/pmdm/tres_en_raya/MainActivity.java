package iesmm.pmdm.tres_en_raya;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{
    // Declaramos el array que guardará los botones
    private Button[] mBotonesTablero;
    // Declaramos el objeto juego y los cuadros de texto que usaremos en el juego
    private JuegoTresEnRaya mJuego;
    private TextView mInfoTexto;
    private TextView playerScore;
    private TextView totalsGames;
    private TextView cpuScore;
    // Establecemos el primer turno al jugador
    private char mTurno = JuegoTresEnRaya.JUGADOR;
    private boolean gameOver = false;
    // Variables para la música que suena en el juego
    private MediaPlayer mJugadorMediaPlayer;
    private MediaPlayer mBackgroundMusicPlayer;
    private MediaPlayer mIntroductionMusic;
    // Variables que contarán victorias/derrotas/partidas totales durante la partida
    private int partidasTotales = 0;
    private int partidasGanadasUsuario = 0;
    private int partidasGanadasCPU = 0;
    // Nivel de dificultad dos valores posibles 0 -> Nivel fácil y 1 -> Nivel medio
    private int dificultad;
    // Esta variable la usaremos en el método silenciarJuego()
    boolean sonidoCambiado = true;
    TextToSpeech objSintetizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Metemos dentro del array los botones del juego usando la utilidad de resource
        mBotonesTablero = new Button[JuegoTresEnRaya.DIM_TABLERO];
        mBotonesTablero[0] = findViewById(R.id.one);
        mBotonesTablero[1] = findViewById(R.id.two);
        mBotonesTablero[2] = findViewById(R.id.three);
        mBotonesTablero[3] = findViewById(R.id.four);
        mBotonesTablero[4] = findViewById(R.id.five);
        mBotonesTablero[5] = findViewById(R.id.six);
        mBotonesTablero[6] = findViewById(R.id.seven);
        mBotonesTablero[7] = findViewById(R.id.eight);
        mBotonesTablero[8] = findViewById(R.id.nine);
        // Inicializamos los cuadros de texto que usaremos más adelante
        mInfoTexto = findViewById(R.id.information);
        playerScore = findViewById(R.id.player_score);
        totalsGames = findViewById(R.id.tie_score);
        cpuScore = findViewById(R.id.computer_score);
        // Intanciamos el objeto juego
        mJuego = new JuegoTresEnRaya();
        // Intanciamos el objeto sintetizador
        objSintetizador = new TextToSpeech(this, this);
        // Instanciamos la múscia que sonará al abrir la app y la iniciamos
        mIntroductionMusic = MediaPlayer.create(this, R.raw.introduction_music);
        mIntroductionMusic.start();
        // Mostramos un mensaje de bienvenida
        mostrarMensaje(getString(R.string.bienvenida_juego));
        // Dormimos el proceso el tiempo justo para que no se sobreponga a la música de fondo cuando se inicie la app
        dormirProceso(mIntroductionMusic.getDuration());
        // Comenzamos el juego
        comenzarJuego();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Instanciamos el efecto al coloar un ficha en el tablero
        mJugadorMediaPlayer = MediaPlayer.create(this, R.raw.effect);
        // Instanciamos la múscia de fondo que sonará en el juego
        mBackgroundMusicPlayer = MediaPlayer.create(this, R.raw.background_music);
        mBackgroundMusicPlayer.setLooping(true);
        mBackgroundMusicPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Liberamos la memoria de los sonidos cuando la app esté en pausa
        mJugadorMediaPlayer.release();
        mBackgroundMusicPlayer.release();
        mIntroductionMusic.release();
    }

    /**
     * Comienza el juego
     *
     * @return null que es necesario en el método dialogoReiniciarPartida()
     */
    private DialogInterface.OnClickListener comenzarJuego() {
        // Limpiamos el tablero lógico
        mJuego.limpiarTablero();
        // Limpiamos el tablero a nivel de vista
        limpiarInterfaz();
        // Mostramos el número de partidas totales y le sumamos 1 a la variable que controla esto
        totalsGames.setText(Integer.toString(partidasTotales));
        partidasTotales++;
        // Le pasamos la dificultad al método controlar turno
        controlarTurno(dificultad);
        return null;
    }

    /**
     * Limpia el tablero a nivel de vista
     */
    private void limpiarInterfaz() {
        for (int i = 0; i < mBotonesTablero.length; i++) {
            mBotonesTablero[i].setText("");
            mBotonesTablero[i].setBackgroundResource(R.drawable.vacio);
            mBotonesTablero[i].setEnabled(true);
        }
    }

    /**
     * Controla el turno del usuario en ese momento
     *
     * @param dificultad se usa cuando la máquina mueve, son dos valores 0 (fácil) y 1 (medio)
     */
    private void controlarTurno(int dificultad) {

        if (mTurno == JuegoTresEnRaya.JUGADOR) {
            // Mostramos quien es el primero en colocar ficha
            mInfoTexto.setText(R.string.primero_jugador);
        } else if (mTurno == JuegoTresEnRaya.MAQUINA) {
            // Guardamos el movimiento que la máquina ha hecho dependiendo de la dificultad
            int casilla = mJuego.getMovimientoMaquina(dificultad);
            // Colocamos la ficha
            colocarFicharEnTablero(JuegoTresEnRaya.MAQUINA, casilla);
            if (!gameOver) {
                // Si el juego jo termina le damos el turno al jugador
                mTurno = JuegoTresEnRaya.JUGADOR;
                mInfoTexto.setText(R.string.turno_jugador);
            }
        }
    }

    /**
     * Coloca la ficha en el tablero
     *
     * @param jugador le pasamos el jugador que esta colocando la ficha en ese momento
     * @param casilla le pasamos la casilla donde el jugador o la máquina pondrá la ficha
     */
    private void colocarFicharEnTablero(char jugador, int casilla) {
        // Colocamos la ficha en el tablero lógico
        mJuego.moverFicha(jugador, casilla);
        // Desactivamos el espacio donde acabamos de poner la ficha
        mBotonesTablero[casilla].setEnabled(false);

        if (jugador == JuegoTresEnRaya.JUGADOR) {
            // Colocamos la ficha a nivel de vista del usuario
            mBotonesTablero[casilla].setBackgroundResource(R.drawable.jugador);
            // Accionamos el efecto de colocar la ficha
            mJugadorMediaPlayer.start();
        } else {
            // Colocamos la ficha a nivel de vista del usuario
            mBotonesTablero[casilla].setBackgroundResource(R.drawable.maquina);
        }
        // Guardamos el valor del estado del juego en ese momento
        int estadoJuego = comprobarEstadoJuego();

        if (estadoJuego == 1 || estadoJuego == 2 || estadoJuego == 0) {
            // Si es 1,2 o 0 significa que la partida ha terminado
            gameOver();
        } else if (estadoJuego == -1) {
            // Si es -1 significa que la partida aún se está jugando
            if (jugador == JuegoTresEnRaya.JUGADOR) {
                // Le damos el siguiente turno a la máquina
                mTurno = JuegoTresEnRaya.MAQUINA;
            } else if (jugador == JuegoTresEnRaya.MAQUINA) {
                // Le damos el turno al jugador
                mTurno = JuegoTresEnRaya.JUGADOR;
            }
            // Controlamos el turno pasándole la dificultad
            controlarTurno(dificultad);
        }
    }

    /**
     * Comprueba el estado del juego
     *
     * @return el estado del juego en ese momento
     */
    private int comprobarEstadoJuego() {
        // Comprobamos el ganador del juego en la clase lógica
        int estado = mJuego.comprobarGanador();

        if (estado == 1) {
            // Si es 1 el usuario a ganado
            // Cambiamos el texto por un mensaje de victoria
            mInfoTexto.setText(R.string.result_human_wins);
            // Mostramos un toast de victoria
            mostrarMensaje(getString(R.string.you_win));
            hablar(getString(R.string.you_win));
            // Sumamos 1 a las partidas ganadas del usuario y lo cambiamos en el cuadro de texto
            partidasGanadasUsuario++;
            playerScore.setText(Integer.toString(partidasGanadasUsuario));
            // Mostramos el cuadro de diálogo para reiniciar
            dialogoReiniciarPartida(R.string.result_human_wins);
        } else if (estado == 2) {
            // Lo mismo que arrba pero con la máquina
            mInfoTexto.setText(R.string.result_computer_wins);
            mostrarMensaje(getString(R.string.you_lose));
            hablar(getString(R.string.you_lose));
            partidasGanadasCPU++;
            cpuScore.setText(Integer.toString(partidasGanadasCPU));
            dialogoReiniciarPartida(R.string.result_computer_wins);
        } else if (estado == 0) {
            // Lo mismo que arriba pero con el empate
            mInfoTexto.setText(R.string.result_tie);
            mostrarMensaje(getString(R.string.you_tie));
            hablar(getString(R.string.you_tie));
            dialogoReiniciarPartida(R.string.result_tie);
        }

        return estado;
    }

    /**
     * Termina el juego
     */
    private void gameOver() {
        gameOver = true;
        for (int i = 0; i < mBotonesTablero.length; i++) {
            mBotonesTablero[i].setEnabled(false);
        }
        comenzarJuego();
    }

    /**
     * Duerme el proceso durante los milisegundos que le pasemos
     * NOTA -> Este método se usa solo al iniciar la app
     *
     * @param milisegundos el tiempo que el proceso se duerme
     */
    private void dormirProceso(int milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra un toast por pantalla al usuario
     *
     * @param mensaje el mensaje que aparece por pantalla
     */
    private void mostrarMensaje(String mensaje) {
        // Creamos el toast
        Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        // Mostramos el toast
        toast.show();
    }

    /**
     * Cuadro de diálogo que aparece cuando terminamos la ronda y nos permite pasar a la siguiente
     *
     * @param mVictoriaDerrotaEmpate el parametro proveniente de recurso que indica el string que muestra el cuadro de diálogo (victoria, derrota o empate)
     */
    private void dialogoReiniciarPartida(int mVictoriaDerrotaEmpate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Cambiamos el title de la ventana
        builder.setTitle(mVictoriaDerrotaEmpate);
        // Mostramos el mensaje que aparecerá en el cuerpo de la ventana
        builder.setMessage(R.string.restart_game);
        // le pasamos la cadena que tendrá el mensaje y ejecutamos el método comenzarJuego() que reinicia la ronda (para eso el return null que vimos antes)
        builder.setPositiveButton(R.string.continue_game, comenzarJuego());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Método para usar el sintetizador de voz
     *
     * @param h mensaje que va a pronunciar
     */
    private void hablar(String h) {
        objSintetizador.speak(h, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void onInit(int i) {
        objSintetizador.setLanguage(Locale.getDefault()); // Idioma
        objSintetizador.setPitch(1.5f); // Entonación de voz
        objSintetizador.setSpeechRate(1); // Velocidad de voz
    }

    /**
     * Lleva a cabo el evento necesario dependiendo del botón que hemos pulsado
     *
     * @param boton el botón que acabamos del pulsar
     */
    public void onClick(View boton) {
        // Guardamos el id del botón que acabamos del pulsar
        int id = boton.getId();

        if (id == R.id.lvl_easy || id == R.id.lvl_medium) {
            // Caso en el que pulsamos los botones de fácil o medio
            // Dejamos de mostrar los botones para elegir la dificultad
            findViewById(R.id.lvl_easy).setVisibility(View.GONE);
            findViewById(R.id.lvl_medium).setVisibility(View.GONE);
            // Mostramos el tablero y el botón para reiniciar la partida
            findViewById(R.id.restart_all).setVisibility(View.VISIBLE);
            findViewById(R.id.buttons).setVisibility(View.VISIBLE);

            switch (id) {
                case R.id.lvl_easy:
                    // Le damos el valor a la variable dificultad dependiendo del botón que hemos pulsado
                    dificultad = 0;
                    // Mostramos un mensaje al usuario dependiendo del nivel de dificultad al que hemos cambiado
                    mostrarMensaje(getString(R.string.establecida_facil));
                    break;
                case R.id.lvl_medium:
                    dificultad = 1;
                    mostrarMensaje(getString(R.string.establecida_medio));
                    break;
            }
        } else if (id == R.id.restart_all) {
            // Caso en el que pulsamos el botónde reniciar partida
            // Devolvemos todas las variables a sus valores iniciales
            partidasTotales = 0;
            partidasGanadasUsuario = 0;
            partidasGanadasCPU = 0;
            // Cambiamos el contenido de los textos
            totalsGames.setText(Integer.toString(partidasTotales));
            cpuScore.setText(Integer.toString(partidasGanadasCPU));
            playerScore.setText(Integer.toString(partidasGanadasUsuario));
            mInfoTexto.setText(R.string.primero_jugador);
            // Mostramos de nuevo los botones de elegir dificultad
            findViewById(R.id.lvl_easy).setVisibility(View.VISIBLE);
            findViewById(R.id.lvl_medium).setVisibility(View.VISIBLE);
            // Ocultamos el panel de juego y el botón para reiniciar partida
            findViewById(R.id.restart_all).setVisibility(View.GONE);
            findViewById(R.id.buttons).setVisibility(View.GONE);
            // findViewById(R.id.silenciar_juego).setBackgroundResource(R.drawable.sonido_activado);
            // Limpiamos el tablero lógico y el de nivel de vista de usuario
            mJuego.limpiarTablero();
            limpiarInterfaz();
        } else if (id == R.id.silenciar_juego) {
            if (sonidoCambiado) {
                // Cambiamos el background del botón por el ícono de sonido desactivado
                findViewById(id).setBackgroundResource(R.drawable.sonido_desactivado);
                sonidoCambiado = false;
                // Paramos la música de fondo
                mBackgroundMusicPlayer.stop();
            } else {
                // Cambiamos el background del botón por el ícono de sonido activado
                findViewById(id).setBackgroundResource((R.drawable.sonido_activado));
                sonidoCambiado = true;
                // Reiniciamos la música de fondo
                onResume();
            }
        } else {
            //Caso en el que pulsamos botones del tablero
            String descripcionBoton = findViewById(id).getContentDescription().toString();
            int casilla = Integer.parseInt(descripcionBoton) - 1;

            if (mBotonesTablero[casilla].isEnabled()) {
                colocarFicharEnTablero(JuegoTresEnRaya.JUGADOR, casilla);
            }
        }
    }
}