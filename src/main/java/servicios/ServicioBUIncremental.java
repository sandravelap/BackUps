package servicios;

import libs.CheckFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

import static java.nio.file.Files.getLastModifiedTime;

public class ServicioBUIncremental {
    public ArrayList<String> copiaIncremental(Path carpetaOrigen) {
        //almacena en la variable la ruta con la que comparar los archivos de carpetaOrigen
        String nombreBUcompleto = "target/BUCompleto";
        //creo una instancia de checkFiles para comprabar si está vacío
        CheckFiles checkFiles = new CheckFiles();
        //inicializo el Array de Strings a devolver con todos los mensajes.
        ArrayList<String> mensaje = new ArrayList<String>();
        //SimpleDateFormat formatea la fecha ya que el símbolo de dos puntos no lo acepta como nombre del directorio
        SimpleDateFormat formateo= new SimpleDateFormat("yyyy-MM-dd '_' HH-mm-ss");
        Date fecha =new Date(System.currentTimeMillis());

        //creo el nombre de la carpeta de destino incluyendo fecha y hora
        String carpetaDestino = "target/BuIncremental_"+formateo.format(fecha);

        //Creo la carpeta si no esta creada.
        if(!Files.exists(Path.of(carpetaDestino))) {
            try {
                Files.createDirectory(Path.of(carpetaDestino));
            } catch (IOException e) {
                mensaje.add("No se pudo crear la carpeta de destino.");
            }
            mensaje.add("Carpeta de destino " + carpetaDestino + " creada.\n");
        }
        //recorro con walk la carpeta Origen para poder comparar la fecha con la carpetaBUCompleto
        if (Files.isDirectory(carpetaOrigen)) {
            try (Stream<Path> paths = Files.walk(carpetaOrigen).skip(1)) {
                paths.forEach(path -> {
                    if (Files.exists(Path.of(nombreBUcompleto, String.valueOf(path.getFileName())))) {
                        try {
                            //si el archivo se ha modificado la comparación devuelve -1
                            if (getLastModifiedTime(path).compareTo(getLastModifiedTime(Path.of(nombreBUcompleto, String.valueOf(path.getFileName())))) < 0) {
                                Files.copy(path, Path.of(carpetaDestino, String.valueOf(path.getFileName())), StandardCopyOption.REPLACE_EXISTING);
                                mensaje.add("Archivo " + String.valueOf(path.getFileName()) + " copiado.");
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        try {
                            Files.copy(path, Path.of(carpetaDestino, String.valueOf(path.getFileName())), StandardCopyOption.REPLACE_EXISTING);
                            mensaje.add("Archivo " + String.valueOf(path.getFileName()) + " copiado.");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                if (checkFiles.isEmpty(Path.of(carpetaDestino))) {
                    Files.delete(Path.of(carpetaDestino));
                    mensaje.add("No se ha modificado ningún archivo, BU no realizado.");
                }else {
                    mensaje.add("BU incremental realizado con éxito");
                }
            }catch (IOException e){
                mensaje.add("Ocurrió un fallo al intentar copiar los archivos");
            }
        }else {mensaje.add("La ruta de origen debe ser un directorio");}
        return mensaje;
    }
}
