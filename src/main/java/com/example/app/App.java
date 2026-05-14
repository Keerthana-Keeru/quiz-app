package com.example.app;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class App {

    public static void main(String[] args) throws Exception {

        HttpServer server =
                HttpServer.create(new InetSocketAddress(8080), 0);

        // HOME PAGE
        server.createContext("/", exchange -> {

            sendFile(exchange, "index.html");

        });
        // CSS FILE
	server.createContext("/style.css", exchange -> {

            InputStream is =
                    App.class.getClassLoader()
                            .getResourceAsStream("style.css");

    	    String css =
                    new String(is.readAllBytes());

    	    exchange.getResponseHeaders()
                    .add("Content-Type", "text/css");

            exchange.sendResponseHeaders(
                    200,
            	    css.getBytes().length
    );

    OutputStream os =
            exchange.getResponseBody();

    os.write(css.getBytes());

    os.close();
});


        // QUIZ PAGE
        server.createContext("/quiz", exchange -> {

            String query =
                    exchange.getRequestURI().getQuery();

            String username =
                    query.split("=")[1];

            username =
                    URLDecoder.decode(
                            username,
                            StandardCharsets.UTF_8
                    );

            String content =
                    readFile("quiz.html");

            content =
                    content.replace(
                            "%USERNAME%",
                            username
                    );

            sendResponse(exchange, content);

        });

        // RESULT PAGE
        server.createContext("/result", exchange -> {

            String query =
                    exchange.getRequestURI().getQuery();

            String[] params =
                    query.split("&");

            String username =
                    params[0].split("=")[1];

            int score = 0;

            String[] answers = {

                    "Build+Tool",
                    "Yes",
                    "Java+Virtual+Machine",
                    "Web+Pages",
                    "Styling",
                    "8080",
                    ".java",
                    "pom.xml",
                    "mvn+clean+install",
                    "Programming+Language"
            };

            for(int i = 1; i <= 10; i++) {

                if(i < params.length) {

                    String value =
                            params[i].split("=")[1];

                    if(value.equals(answers[i - 1])) {

                        score++;
                    }
                }
            }

            // TERMINAL OUTPUT
            System.out.println("--------------------------------");

            System.out.println(
                    "Student Name : " + username
            );

            System.out.println(
                    "Score        : " + score + "/10"
            );

            System.out.println("--------------------------------");

            String content =
                    readFile("result.html");

            content =
                    content.replace(
                            "%USERNAME%",
                            username
                    );

            content =
                    content.replace(
                            "%SCORE%",
                            String.valueOf(score)
                    );

            sendResponse(exchange, content);

            // AUTO STOP SERVER
            System.out.println(
                    "Quiz Finished Successfully"
            );

            new Thread(() -> {

                try {

                    Thread.sleep(3000);

                    System.out.println(
                            "Server stopping..."
                    );

                    server.stop(0);

                    System.exit(0);

                } catch(Exception e) {

                    e.printStackTrace();
                }

            }).start();

        });

        server.start();

        System.out.println(
                "Server started at http://localhost:8080"
        );
    }

    static void sendFile(
            HttpExchange exchange,
            String filename
    ) throws IOException {

        String response =
                readFile(filename);

        sendResponse(exchange, response);
    }

    static void sendResponse(
            HttpExchange exchange,
            String response
    ) throws IOException {

        exchange.sendResponseHeaders(
                200,
                response.getBytes().length
        );

        OutputStream os =
                exchange.getResponseBody();

        os.write(response.getBytes());

        os.close();
    }

    static String readFile(
            String filename
    ) throws IOException {

        InputStream is =
                App.class
                        .getClassLoader()
                        .getResourceAsStream(filename);

        BufferedReader br =
                new BufferedReader(
                        new InputStreamReader(is)
                );

        StringBuilder sb =
                new StringBuilder();

        String line;

        while((line = br.readLine()) != null) {

            sb.append(line);
        }

        return sb.toString();
    }
}
