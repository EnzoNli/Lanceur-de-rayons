import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CheckScene {
    public static void main(String[] args) throws IOException, FileNotFoundException{
        File fichier = new File(args[0]);
        FileReader f_reader = new FileReader(fichier);
        BufferedReader br = new BufferedReader(f_reader);

        String ligne;
        String[] parse;
        while((ligne = br.readLine()) != null){
            parse = ligne.split(" ");
            if(parse[0] != "#"){
                switch (parse[0]) {
                    case "size":
                        
                        break;
                
                    default:
                        break;
                }
            }
        }




        f_reader.close();
        br.close();
    }
}
