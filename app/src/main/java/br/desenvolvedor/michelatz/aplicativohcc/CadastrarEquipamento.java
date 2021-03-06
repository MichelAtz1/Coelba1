package br.desenvolvedor.michelatz.aplicativohcc;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import java.util.ArrayList;

import br.desenvolvedor.michelatz.aplicativohcc.Adapter.AdapterListViewConsumidores;
import br.desenvolvedor.michelatz.aplicativohcc.ClassesExtras.Helper;
import br.desenvolvedor.michelatz.aplicativohcc.Modelo.DadosGerais;

public class CadastrarEquipamento extends AppCompatActivity {

    private Spinner spnTiposEquipamento;
    private TextView textNumeroPlacaEquipamento;
    private ListView listViewConsumidor;

    private TextView textPotenciaTransformador, textCIA;
    private EditText edtPotenciaTransformador, edtCIA;

    ArrayList<String> tipoEquipamento = new ArrayList<String>();
    ArrayAdapter adapter;

    SQLiteDatabase db;
    String BANCO = "banco.db", TABELAEQUIPAMENTO = "equipamento", TABELACONSUMIDOR = "consumidor";
    String idPoste, primeiraInsercao = "0", valorPagina = "0";

    private AdapterListViewConsumidores adapterListViewConsumidores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumidor_equipamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Aplicativo HCC");
        toolbar.setSubtitle("Dados do Equipamento");

        //Supote ao Toobar(Apresenta Icone voltar, na barra de cima da tela)
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        textPotenciaTransformador = (TextView) findViewById(R.id.textPotenciaTransformador);
        textCIA = (TextView) findViewById(R.id.textCIA);
        edtPotenciaTransformador = (EditText) findViewById(R.id.edtPotenciaTransformador);
        edtCIA = (EditText) findViewById(R.id.edtCIA);

        listViewConsumidor = (ListView) findViewById(R.id.listViewConsumidor);

        tipoEquipamento.add("Selecione um Tipo");
        tipoEquipamento.add("Transformador");
        tipoEquipamento.add("Chave - Fusível");
        tipoEquipamento.add("Capacitor");
        tipoEquipamento.add("Para-Raios");
        tipoEquipamento.add("Chave a Óleo");
        tipoEquipamento.add("Chave - Faca");
        tipoEquipamento.add("Chave - Fusível - Repetidora");
        tipoEquipamento.add("Regulador de tensão");
        tipoEquipamento.add("Religador");


        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        idPoste = sharedpreferences.getString("idPosteKey", null);

        spnTiposEquipamento = (Spinner) findViewById(R.id.spnTiposEquipamento);
        textNumeroPlacaEquipamento = (TextView) findViewById(R.id.textNumeroPlacaEquipamento);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoEquipamento);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTiposEquipamento.setAdapter(adapter);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        // Verifica de existe dados vindos da tela anterior
        if (getIntent().getStringExtra("USERTELA") != null) {

            // Verifica se estes dados são dados que foram inseridos antes de ir para a pagina anterior
            if (getIntent().getStringExtra("USERTELA").equals("10")) {
                if (getIntent().getStringExtra("placa") != null) {
                    textNumeroPlacaEquipamento.setText(bundle.getString("placa"));
                }
                if (getIntent().getStringExtra("tipo") != null) {
                    int spinnerPosition = adapter.getPosition(bundle.getString("tipo"));
                    spnTiposEquipamento.setSelection(spinnerPosition);
                }
                if (getIntent().getStringExtra("cia") != null) {
                    edtCIA.setText(bundle.getString("cia"));
                }
                if (getIntent().getStringExtra("potencia") != null) {
                    edtPotenciaTransformador.setText(bundle.getString("potencia"));
                }
            }

            // Verifica se este equipamento esta sendo editado
            if (getIntent().getStringExtra("USERTELA").equals("EDITAR")) {
                valorPagina = "1";
                preencheEquipamentoEdicao();
            }
        }
        inflaListaConsumidores();
        OnItemSelectedListener countrySelectedListener = new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> spinner, View container,
                                       int position, long id) {
                //mTvCountry.setText(countries[position]);

                if(spnTiposEquipamento.getSelectedItem().toString().equals("Transformador")){
                    Log.d("mesdagem","Consumidor: "+spnTiposEquipamento.getSelectedItem().toString());
                    textPotenciaTransformador.setVisibility(View.VISIBLE);
                    textCIA.setVisibility(View.VISIBLE);
                    edtPotenciaTransformador.setVisibility(View.VISIBLE);
                    edtCIA.setVisibility(View.VISIBLE);
                }else {
                    textPotenciaTransformador.setVisibility(View.GONE);
                    textCIA.setVisibility(View.GONE);
                    edtPotenciaTransformador.setVisibility(View.GONE);
                    edtCIA.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        };

        // Setting ItemClick Handler for Spinner Widget
        spnTiposEquipamento.setOnItemSelectedListener(countrySelectedListener);

    }

    //Metodo que pega a ação no clique do Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Tem certeza que deseja sair desta aba? Os dados ainda não foram salvos");

                alertDialogBuilder.setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(CadastrarEquipamento.this, InseriPoste.class);
                                intent.putExtra("USERTELA", "EDITAR");
                                CadastrarEquipamento.this.startActivity(intent);
                                finish();
                            }
                        });

                alertDialogBuilder.setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            default:
                break;
        }
        return true;
    }

    //Metodo que busca no banco, todos os consumidores daquele poste, para serem inseridos em lista
    private void inflaListaConsumidores() {
        ArrayList<DadosGerais> itensDoc = new ArrayList<DadosGerais>();

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);

        // Select que busca na tabela Consumidor, quantos consumidores existe neste poste(idPoste)
        Cursor linhas = db.rawQuery("SELECT * FROM " + TABELACONSUMIDOR + " WHERE IDPOSTE = " + idPoste + ";", null);
        String texto = "";

        // Vai até a ultima linha fo select, inserindo na lista, cada consumidor com seus respectivos dados
        if (linhas.moveToFirst()) {
            do {
                String idPoste = linhas.getString(0);
                String lado = linhas.getString(1);

                if (lado != null) {
                    if (lado.equals("E")) {
                        texto = "Esquerda";
                    } else if (lado.equals("D")) {
                        texto = "Direita";
                    }
                }

                DadosGerais itemDoc = new DadosGerais(idPoste, texto);
                itensDoc.add(itemDoc);
            }
            while (linhas.moveToNext());
        }
        adapterListViewConsumidores = new AdapterListViewConsumidores(this, itensDoc);
        listViewConsumidor.setAdapter(adapterListViewConsumidores);
        listViewConsumidor.setCacheColorHint(Color.TRANSPARENT);

        linhas.close();

        // Aumenta o tamanho da tela, conforme a quantidade de Consumidores
        Helper.getListViewSize(listViewConsumidor);
        db.close();
    }

    //Metodo que pega a ação do botão "Próximo"
    public void irProximoRetirarFoto(View v) {

        // verifica se ente Equipamento esta sendo inseri novo ou é uma edição
        if (valorPagina.equals("1")) { // Se sim, chama o metodo de update e avisa a proxima tela que é uma edição do poste
            updateEquipamento();
        } else { // Se não, chama o metodo de inserção
            inseriEquipamento();
        }
    }

    //Metodo de inserção de um Equipamento
    private void inseriEquipamento() {
        String numeroPlacaEquipamento = textNumeroPlacaEquipamento.getText().toString();
        String TipoPosteSelecionado = spnTiposEquipamento.getSelectedItem().toString();
        String potencia = "";
        String cia = "";

        if (TipoPosteSelecionado.equals("Selecione um Tipo")) {
            TipoPosteSelecionado = "";

        }else if(TipoPosteSelecionado.equals("Transformador")){
            potencia = edtPotenciaTransformador.getText().toString();
            cia = edtCIA.getText().toString();
        }

        Log.d("mensagem","vai funcionarr"+numeroPlacaEquipamento.trim().equals(""));

        if(TipoPosteSelecionado.equals("Transformador") && numeroPlacaEquipamento.trim().equals("")){
            Toast.makeText(this, "Quando for Transformador, deve-se inserir o numero da placa obrigatóriamente!", Toast.LENGTH_SHORT).show();
        }else{
            db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
            ContentValues values = new ContentValues();
            values.put("TIPO", TipoPosteSelecionado);
            values.put("PLACA", numeroPlacaEquipamento);
            values.put("POTENCIA", potencia);
            values.put("CIA", cia);
            values.put("IDPOSTE", idPoste);

            db.insert(TABELAEQUIPAMENTO, null, values);
            db.close();

            Intent intent = new Intent(this, Localizacao.class);
            this.startActivity(intent);
            finish();
        }
    }

    //Metodo de edição de um Equipamento
    private void updateEquipamento() {
        if (primeiraInsercao.equals("1")) {
            inseriEquipamento();
        } else {
            String potencia = "";
            String cia = "";
            String numeroPlacaEquipamento = textNumeroPlacaEquipamento.getText().toString();
            String TipoPosteSelecionado = spnTiposEquipamento.getSelectedItem().toString();
            if (TipoPosteSelecionado.equals("Selecione um Tipo")) {
                TipoPosteSelecionado = "";
            }else if(TipoPosteSelecionado.equals("Transformador")){
                potencia = edtPotenciaTransformador.getText().toString();
                cia = edtCIA.getText().toString();
            }

            if(TipoPosteSelecionado.equals("Transformador") && numeroPlacaEquipamento.trim().equals("")){
                Toast.makeText(this, "Quando for Transformador, deve-se inserir o numero da placa obrigatóriamente!", Toast.LENGTH_SHORT).show();
            }else {
                db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
                ContentValues values = new ContentValues();
                values.put("TIPO", TipoPosteSelecionado);
                values.put("PLACA", numeroPlacaEquipamento);
                values.put("POTENCIA", potencia);
                values.put("CIA", cia);
                db.update(TABELAEQUIPAMENTO, values, "IDPOSTE=" + idPoste, null);
                db.close();

                Intent intent = new Intent(this, Localizacao.class);
                intent.putExtra("USERTELA", "EDITAR");
                this.startActivity(intent);
                finish();
            }
        }
    }

    // Metodo que pega ação do botão, para inserir um novo Consumidor
    public void adicionarConsumidor(View v) {
        String numeroPlacaEquipamento = textNumeroPlacaEquipamento.getText().toString();
        String TipoPosteSelecionado = spnTiposEquipamento.getSelectedItem().toString();

        String CIA = edtCIA.getText().toString();
        String potencia = edtPotenciaTransformador.getText().toString();

        Intent intent = new Intent(this, CadastrarConsumidores.class);

        //avisa a tela de consumidores que estes dados deveram ser retornados
        intent.putExtra("USERTELA", "10");
        intent.putExtra("tipo", TipoPosteSelecionado);
        intent.putExtra("placa", numeroPlacaEquipamento);
        intent.putExtra("cia", CIA);
        intent.putExtra("potencia", potencia);

        // Verifica se é uma edição de Equipamento
        if (valorPagina != null && valorPagina.equals("1")) { // Se sim, avisa a próxima tela
            intent.putExtra("edit", "1");
        }
        this.startActivity(intent);
        finish();
    }

    //Metodo responsavel por pegar ação do botão voltar do smartfone
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Tem certeza que deseja sair desta aba? Os dados ainda não foram salvos");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(CadastrarEquipamento.this, InseriPoste.class);
                        intent.putExtra("USERTELA", "EDITAR");
                        CadastrarEquipamento.this.startActivity(intent);
                        finish();
                        CadastrarEquipamento.super.onBackPressed();
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Pega ação do botão de exclusão de um consumidor
    public void deletaItem(View v) {
        adapterListViewConsumidores.removeItem((Integer) v.getTag());

        // Pega o id do consumidor selecionado
        String idMensagem = adapterListViewConsumidores.idSelecionado;
        confirmarDelete(idMensagem);
    }

    // Pega ação do botão de edição de um consumidor
    public void editaItem(View v) {
        adapterListViewConsumidores.editaItem((Integer) v.getTag());

        // pega o id do Consumidor selecionado
        String idMensagem = adapterListViewConsumidores.idSelecionado;

        // Pega os dados do Equipamento, caso tenham sido preenchidos
        String numeroPlacaEquipamento = textNumeroPlacaEquipamento.getText().toString();
        String TipoPosteSelecionado = spnTiposEquipamento.getSelectedItem().toString();

        String CIA = edtCIA.getText().toString();
        String potencia = edtPotenciaTransformador.getText().toString();

        Intent intent = new Intent(this, CadastrarConsumidores.class);
        // Avisa a proxima tela, que é uma edição
        intent.putExtra("USERTELA", "EDITAR");
        intent.putExtra("tipo", TipoPosteSelecionado);
        intent.putExtra("placa", numeroPlacaEquipamento);
        intent.putExtra("cia", CIA);
        intent.putExtra("potencia", potencia);
        intent.putExtra("id", idMensagem);

        // Verifica se nesta pagina esta sendo editado
        if (valorPagina != null && valorPagina.equals("1")) { // Se sim, avisa a proxima pagina
            intent.putExtra("edit", "1");
        }

        this.startActivity(intent);
        finish();
    }

    // Metodo responsavel em abrir um alert, para a confirmação da exclusão de um Consumidor
    private void confirmarDelete(final String idMensagem) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Tem certeza que deseja deletar este Consumidor?");

        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deletarMensagem(idMensagem);
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //Metodo que exclui o consumidor do banco
    private void deletarMensagem(final String idMens) {
        int idExcluido = Integer.parseInt(idMens.toString());
        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        db.execSQL("DELETE FROM " + TABELACONSUMIDOR + " WHERE ID = " + idExcluido + "");
        db.close();

        // Infla a nova lista de consumidores, obvio que sem o consumidor recém excluido
        inflaListaConsumidores();
    }

    //Metodo que inseri os dados do Equipamento, nos seus devidos campos
    public void preencheEquipamentoEdicao() {
        String tipoEquipamento = null;
        String numeroPlaca = null;
        String cia = null;
        String potencia = null;

        db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
        Cursor linhas = db.rawQuery("SELECT * FROM " + TABELAEQUIPAMENTO + " WHERE IDPOSTE = " + idPoste + ";", null);

        int contador = 0;

        if (linhas.moveToFirst()) {
            do {
                tipoEquipamento = linhas.getString(1);
                numeroPlaca = linhas.getString(2);
                potencia = linhas.getString(5);
                cia = linhas.getString(6);
                contador++;

            }
            while (linhas.moveToNext());
            linhas.close();
        }
        db.close();
        if (contador == 0) {
            primeiraInsercao = "1";
        }

        if (numeroPlaca != null) {
            textNumeroPlacaEquipamento.setText(numeroPlaca);
        }
        if (tipoEquipamento != null) {
            int spinnerPosition2 = adapter.getPosition(tipoEquipamento);
            spnTiposEquipamento.setSelection(spinnerPosition2);
        }

        if (potencia != null) {
            edtPotenciaTransformador.setText(potencia);
        }

        if (cia != null) {
            edtCIA.setText(cia);
        }
    }

}
