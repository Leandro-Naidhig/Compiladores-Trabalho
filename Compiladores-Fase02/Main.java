/*
    Bacharelado em Ciência da Computação

    Universidade Federal de São Carlos

    Campus Sorocaba

    Projeto Prático Fase 1 - Compiladores

    Integrantes:

    Nome: [Chady Chaito] (https://github.com/chadychaito) RA: 613697
    Nome: [Gustavo Buoro Branco de Souza] (https://github.com/Gustavobbs/) RA: 726533
    Nome: [José Gabriel Oliveira Santana] (https://github.com/Eetrexx/) RA: 620459
    Nome: [Leandro Naidhig] (https://github.com/Leandro-Naidhig/) RA: 726555

*/
import AST.*;
import java.io.*;

public class Main {

  public static void main( String []args ) {

    File file;
    FileReader stream;
    int numChRead;
    Program program;
    String arquivo_entrada;

    if ( args.length != 2 ) {
      System.out.println("Usage:\n Main input output");
      System.out.println("input is the file to be compiled");
      System.out.println("output is the file where the generated code will be stored");

    } else {

      file = new File(args[0]);
      arquivo_entrada = args[0];

      if ( !file.exists() || ! file.canRead() ) {
        System.out.println("Either the file " + args[0] + " does not exist or it cannot be throw new RuntimeException()");
      }

      try {
        stream = new FileReader(file);
      } catch ( FileNotFoundException e ) {
        System.out.println("Something wrong: file does not exist anymore");
        throw new RuntimeException();
      }

      // one more character for ’\0’ at the end that will be added by the
      // compiler
      char []input = new char[ (int ) file.length() + 1 ];

      try {
        numChRead = stream.read( input, 0, (int ) file.length() );
      } catch ( IOException e ) {
        System.out.println("Error reading file " + args[0]);
        throw new RuntimeException();
      }

      if ( numChRead != file.length() ) {
        System.out.println("Read error");
        throw new RuntimeException();
      }

      try {
        stream.close();
      } catch ( IOException e ) {
        System.out.println("Error in handling the file " + args[0]);
        throw new RuntimeException();
      }

      Compiler compiler = new Compiler();
      FileOutputStream outputStream;

      try {
        outputStream = new FileOutputStream(args[1]);
      } catch ( IOException e ) {
        System.out.println("File " + args[1] + " could not be opened for writing");
        throw new RuntimeException();
      }

      PrintWriter printWriter = new PrintWriter(outputStream);
      program = null;

      // the generated code goes to a file and so are the errors
      try {
        program = compiler.compile(input, printWriter, arquivo_entrada);
      } catch ( RuntimeException e ) {
        System.out.println(e);
      }

      if (program != null && compiler.getFlag()) {

        PW pw = new PW();
        pw.set(printWriter);
        program.genC(pw);

        if ( printWriter.checkError() ) {
          System.out.println("There was an error in the output");
        }
      }
    }
  }
}
