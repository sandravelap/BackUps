package cmd;

import servicios.ServicioBUCompleto;
import servicios.ServicioBUIncremental;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuBackups {
    private Scanner scanner = new Scanner(System.in);
    private ServicioBUCompleto servicioBUCompleto = new ServicioBUCompleto();
    private ServicioBUIncremental servicioBUIncremental = new ServicioBUIncremental();
    private boolean salir = false;
    private Path rutaOrigen = Path.of("src/main/resources/Origen");

    static void mostrarEnPantalla(String mensaje) {
        System.out.println(mensaje);
    }

    static void mostrarLista(ArrayList<String> mensajes) {
        for (String mensaje : mensajes){
            mostrarEnPantalla(mensaje);
        }
    }

    public void muestraMenu(){
        String opcion;
        do {
            mostrarEnPantalla("Elige una opcion:");
            mostrarEnPantalla("1. Realizar un BU Completo de la carpeta Origen");
            mostrarEnPantalla("2. Realizar un BU Incremental de la carpeta Origen");
            mostrarEnPantalla("0. Salir");
            opcion = this.pideOpcion();
            this.procesaOpcion(opcion);
        } while (!salir);
    }

    private String pideOpcion() {
        return this.scanner.nextLine();
    }

    private void procesaOpcion(String opcion) {
        switch (opcion) {
            case "0" -> salir = true;
            case "1" -> mostrarEnPantalla(servicioBUCompleto.buCompleto(rutaOrigen));
            case "2" -> mostrarLista(servicioBUIncremental.copiaIncremental(rutaOrigen));
            default -> mostrarEnPantalla("Opcion no valida");
        }
    }
}
