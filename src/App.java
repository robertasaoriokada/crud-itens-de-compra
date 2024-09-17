import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import Model.ItemDeCompra;
import Service.ListaDeCompras;



public class App {
    static ListaDeCompras lista = new ListaDeCompras();
    public static void main (String args[]) throws IOException {
       
        lista.adicionarItem(new ItemDeCompra("Sabonete", 2, 3.2));
        lista.adicionarItem(new ItemDeCompra("Whey", 1, 100.90));
        lista.adicionarItem(new ItemDeCompra("Pão de queijo", 2, 10.0));
        lista.adicionarItem(new ItemDeCompra("Tênis", 4, 250));
        lista.adicionarItem(new ItemDeCompra("Shampoo", 4, 15.0));
        lista.adicionarItem(new ItemDeCompra("Cebola", 5, 0.54));
        lista.adicionarItem(new ItemDeCompra("Pão", 6, 5.5));

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/edit", new EditHandler());
        server.createContext("/enviar", new PostHandler());
        server.createContext("/delete", new DeleteHandler());
        server.createContext("/", new GetHandler());
        server.createContext("/options", new OptionsHandler()); // Adicione este contexto


        server.setExecutor(null); // Usa um executor padrão
        server.start();
        System.out.println("Servidor iniciado na porta 8080");

        lista.listarItens();
        System.out.println("Preço total das compras: " + lista.precoTotal());
        lista.editarItem(new ItemDeCompra("Whey2", 2, 120.90), 2);
        lista.listarItens();
        lista.deletarItem(6);
        lista.listarItens();
        System.out.println("Preço total das compras: " + lista.precoTotal());
        
    }

    static class OptionsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                // Responde com sucesso para solicitações OPTIONS
                exchange.sendResponseHeaders(200, -1); // -1 para indicar que não há corpo
            } else {
                String response = "Método não permitido";
                exchange.sendResponseHeaders(405, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    static class EditHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(200, -1); // Responde para OPTIONS
            return;
        }
            if("POST".equals(exchange.getRequestMethod())){
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
                System.out.println(requestBody);
                String json = requestBody.toString();
                int id = Integer.parseInt(extractValueFromJson(json, "id"));
                String produto = extractValueFromJson(json, "produto");
                int unidades = Integer.parseInt(extractValueFromJson(json, "unidades"));
                double preco = Double.parseDouble(extractValueFromJson(json, "preco"));

                lista.editarItem(new ItemDeCompra(produto, unidades, preco), id);
            }
            

        }
    
    }

    static class PostHandler implements HttpHandler {

        @Override
        
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1); // Responde para OPTIONS
                return;
            }
            if("POST".equals(exchange.getRequestMethod())){

                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
                System.out.println(requestBody);
                String json = requestBody.toString();
                String produto = extractValueFromJson(json, "produto");
                int unidades = Integer.parseInt(extractValueFromJson(json, "unidades"));
                double preco = Double.parseDouble(extractValueFromJson(json, "preco"));

                lista.adicionarItem(new ItemDeCompra(produto, unidades, preco));
            }
        }   
    }

    static class DeleteHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1); // Responde para OPTIONS
                return;
            }
            if("POST".equals(exchange.getRequestMethod())){
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8));
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
                System.out.println(requestBody);
                String json = requestBody.toString();
                int id = Integer.parseInt(extractValueFromJson(json, "id"));
                lista.deletarItem(id);
            }
        }   
    }

    static class GetHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(200, -1); // Responde para OPTIONS
                return;
            }
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = generateJsonResponse();
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                String response = "Método não permitido";
                exchange.sendResponseHeaders(405, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    private static String extractValueFromJson(String json, String key) {
        String searchString = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchString) + searchString.length();
        int endIndex = json.indexOf("\"", startIndex);
        return json.substring(startIndex, endIndex);
    }


     // Método para gerar a resposta JSON com a lista de itens
     private static String generateJsonResponse() {
        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("{\"items\":[");
    
        for (int i = 0; i < lista.size(); i++) {
            ItemDeCompra item = lista.getItem(i);
            jsonBuilder.append("{")
                .append("\"id\":\"").append(item.getId()).append("\",")
                .append("\"nome\":\"").append(item.getNome()).append("\",")
                .append("\"unidades\":").append(item.getUnidades()).append(",")
                .append("\"preco\":").append(item.getPreco())
                .append("}");
            if (i < lista.size() - 1) {
                jsonBuilder.append(",");
            }
        }
    
        jsonBuilder.append("]}");
        return jsonBuilder.toString();
    }

    private static void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

}