package Model;

public class ItemDeCompra {

    private int id;
    private String nome;
    private int unidades;
    private double preco;

    
    public ItemDeCompra(String nome, int unidades, double preco) {
        this.nome = nome;
        this.unidades = unidades;
        this.preco = preco;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public double getPreco() {
        return preco;
    }
    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    @Override
    public String toString() {
        return "ItemDeCompra{" +
                "id=" + id +
                "nome='" + nome + '\'' +
                ", quantidade=" + unidades +
                ", preco=" + preco +
                '}';
    }

    
}
