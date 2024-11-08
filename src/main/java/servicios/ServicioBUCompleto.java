package servicios;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ServicioBUCompleto {
    public String buCompleto(Path carpetaOrigen){
        String mensaje="";
        ArrayList<String> copiados = new ArrayList<>();
        String carpetaDestino = "target/BUCompleto";
        if (!Files.exists(Path.of(carpetaDestino))) {
            try {
                Files.createDirectory(Path.of(carpetaDestino));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (Files.isDirectory(carpetaOrigen)) {
            try (Stream<Path> paths = Files.list(carpetaOrigen)) {
                paths.forEach(path -> {
                    try {
                        Files.copy(path, Path.of(carpetaDestino, String.valueOf(path.getFileName())), StandardCopyOption.REPLACE_EXISTING);
                        copiados.add(path.getFileName().toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                for (String copiado : copiados){
                    mensaje += "Archivo " + copiado + " copiado. \n";
                }
                mensaje +="BU completo realizado con éxito";
            }catch (IOException e){
                mensaje="Ocurrió un fallo al intentar copiar los archivos";
            }
        }else {mensaje="La ruta de origen debe ser un directorio";}

        return mensaje;
    }
}
