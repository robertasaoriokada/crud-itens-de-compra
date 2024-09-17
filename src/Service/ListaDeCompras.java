package Service;

import java.util.ArrayList;
import java.util.List;

import Model.ItemDeCompra;

public class ListaDeCompras {
    List<ItemDeCompra> listaDeCompras = new ArrayList<>();

    public int size() {
        return listaDeCompras.size();
    }

    public ItemDeCompra getItem(int a){
        return listaDeCompras.get(a);
    }

    public void adicionarItem(ItemDeCompra item){
        if(listaDeCompras.isEmpty()){
            item.setId(1);
        } else {
            item.setId(listaDeCompras.size() + 1);
        }
        listaDeCompras.add(item);
    }

  

    public void editarItem(ItemDeCompra item, int Id){
        
        for (ItemDeCompra itemDeCompra : listaDeCompras) {
            if(itemDeCompra.getId() == Id){
                itemDeCompra.setNome(item.getNome());
                itemDeCompra.setPreco(item.getPreco());
                itemDeCompra.setUnidades(item.getUnidades());
            }
        }

    }

    public void deletarItem(int Id){
        for (ItemDeCompra itemDeCompra : listaDeCompras) {
            if(itemDeCompra.getId() == Id){
                listaDeCompras.remove(itemDeCompra);
            }
      
        }

        if(!listaDeCompras.isEmpty()){
            int i = 1;
            for (ItemDeCompra itemDeCompra : listaDeCompras) {
                itemDeCompra.setId(i);
                i++;
            }
        }
        
        
    }

    public void listarItens(){
        for (ItemDeCompra itemDeCompra : listaDeCompras) {
            System.out.println("-" + itemDeCompra.getId() + " " + itemDeCompra.getNome() +" " + itemDeCompra.getUnidades() + " " + itemDeCompra.getPreco());
        }
   
    }

    public double precoTotal(){
        double soma = 0;
        for (ItemDeCompra itemDeCompra : listaDeCompras) {
            double x = itemDeCompra.getUnidades() * itemDeCompra.getPreco();
            soma += x;
        }
        return soma;
    }



}
