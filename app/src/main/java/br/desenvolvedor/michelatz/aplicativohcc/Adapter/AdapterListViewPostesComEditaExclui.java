package br.desenvolvedor.michelatz.aplicativohcc.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import br.desenvolvedor.michelatz.aplicativohcc.Modelo.DadosGerais;
import br.desenvolvedor.michelatz.aplicativohcc.R;

public class AdapterListViewPostesComEditaExclui extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<DadosGerais> itens;

    public static String idSelecionado;

    public AdapterListViewPostesComEditaExclui(Context context, ArrayList<DadosGerais> itens) {

        this.itens = itens;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return itens.size();
    }

    public DadosGerais getItem(int position) {
        return itens.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        DadosGerais item = itens.get(position);
        view = mInflater.inflate(R.layout.list_item_pontos_exclusao, null);
        ((TextView) view.findViewById(R.id.txtMensagem)).setText(item.getTexto());
        ((ImageButton) view.findViewById(R.id.btnDeletePonto)).setTag(position);
        ((ImageButton) view.findViewById(R.id.btnEditarPonto)).setTag(position);
        return view;
    }

    public void removeItemPoste(int positionToRemove) {
        DadosGerais item = itens.get(positionToRemove);
        idSelecionado = item.getId();
        notifyDataSetChanged();
    }

    public void editaItemPoste(int positionToRemove) {
        DadosGerais item = itens.get(positionToRemove);
        idSelecionado = item.getId();
        notifyDataSetChanged();
    }

}
