package com.example.carlos.projetocultural.pagination

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager

/*
 esta classe. Para habilitar Pagination, devemos detectar o usuário alcançando o final da lista ( RecyclerView). PaginationScrollListener nos permite fazer isso.
 */
abstract class PaginationScrollListener(internal var layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    /*
    A onScrolled()lógica é a parte mais importante de toda a sua lógica de Paginação. Portanto, verifique se você está fazendo certo. O trecho de chave que contém a lógica de paginação é o seguinte.
    Método de retorno de chamada a ser invocado quando o RecyclerView foi rolado. */
    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount // pega a quantidade de itens visivel..acho
        val totalItemCount = layoutManager.itemCount //total de item na pagina...acho
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition() //procura a primeira posicao visible....

        //abstrato essas variaveis
        if (!isLoading() && !isLastPage()) {//não tiver mais carregando, e não for a ultima pagina, carrga mais
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                loadMoreItems()
            }
        }
    }// para ocultar o FAB quando rolar --> https://stackoverflow.com/questions/43797657/recyclerview-addonscrolllistener

    //abstrata para fazer a implementação em outra classe
    protected abstract fun loadMoreItems()

    abstract fun  getTotalPageCount():Int

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}