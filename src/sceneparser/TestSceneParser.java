package sceneparser;

import java.io.IOException;

public class TestSceneParser {
    public static void main(String[] args) throws IOException {
        SceneParser s = new SceneParser(args[0]);
        s.parse();
        System.out.println(s.toString());
    }
}
