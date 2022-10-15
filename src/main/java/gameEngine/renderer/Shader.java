package gameEngine.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {

    private int shaderProgramID;
    private boolean beingUsed = false;

    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    public Shader(String filepath, boolean compile){
        this.filePath = filepath;

        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            //Split file source into vertex and fragment code
            String[] splitStr = source.split("(#type)( )+([a-zA-Z]+)");

            //Find args for first #type
            int wordIndex = source.indexOf("#type") + 6;
            int endOfLine = source.indexOf("\r\n", wordIndex);
            String firstPattern = source.substring(wordIndex, endOfLine).trim();

            //Find args for second #type
            wordIndex = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\r\n", wordIndex);
            String secondPattern = source.substring(wordIndex, endOfLine).trim();

            switch(firstPattern.toLowerCase()){
                case "vertex" -> vertexSource = splitStr[1];
                case "fragment" -> fragmentSource = splitStr[1];
                default -> throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            switch(secondPattern.toLowerCase()){
                case "vertex" -> vertexSource = splitStr[2];
                case "fragment" -> fragmentSource = splitStr[2];
                default -> throw new IOException("Unexpected token '" + secondPattern + "'");
            }

            if (compile) compileAndLink();
        } catch (IOException e) {
            assert false : "ERROR: Could not open file for shader '" + filePath + "'";
        }
    }

    //Compile and link shaders
    public void compileAndLink(){
        int vertexID, fragmentID;

        //Load and compile vertex and fragment shaders
        vertexID = compileShader(vertexSource, GL_VERTEX_SHADER);
        fragmentID = compileShader(fragmentSource, GL_FRAGMENT_SHADER);

        //Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        //Check for linking info
        int success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE){
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: `" + filePath + "`\n\tLinking of shaders failed");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    private int compileShader(String shaderSource, int shaderType){

        int shaderID;

        //Load and compile shader
        shaderID = glCreateShader(shaderType);
        //Pass the shader source to the gpu
        glShaderSource(shaderID, shaderSource);
        glCompileShader(shaderID);

        //Check for compilation shader error
        int success = glGetShaderi(shaderID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int len = glGetShaderi(shaderID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: `" + filePath + "`\n\tVertex shader compilation failed");
            System.out.println(glGetShaderInfoLog(shaderID, len));
            assert false : "";
        }

        return shaderID;
    }

    public void use(){
        if (!beingUsed){
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }

    public void detach(){
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String name, Matrix4f mat4){
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        //Flatten matrix to 1D array
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);

        glUniformMatrix4fv(location, false, matBuffer);
    }

    public void uploadVec4f(String name, Vector4f vec){
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform4f(location, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadFloat(String name, float val){
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1f(location, val);
    }

    public void uploadInt(String name, int val){
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1i(location, val);
    }

    public void uploadVec3f(String name, Vector3f vec){
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform3f(location, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String name, Vector2f vec){
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform2f(location, vec.x, vec.y);
    }

    public void uploadMat3f(String name, Matrix3f mat3){
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        //Flatten matrix to 1D array
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);

        glUniformMatrix3fv(location, false, matBuffer);
    }

    public void uploadTexture(String name, int textureSlot){
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1i(location, textureSlot);
    }

    public void uploadIntArray(String name, int[] array){
        int location = glGetUniformLocation(shaderProgramID, name);
        use();
        glUniform1iv(location, array);
    }
}
