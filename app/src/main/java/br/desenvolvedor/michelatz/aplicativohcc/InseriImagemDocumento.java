package br.desenvolvedor.michelatz.aplicativohcc;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InseriImagemDocumento extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private Spinner spnNomeDocumentos;
    String caminhoImagem = "inicial", BANCO = "banco.db", TABELADOCUMENTO = "documento", imgString, nomeDocumento;
    String valorCroqui = "0";
    String numeroCheck;
    static final int CODIGO_REQUISICAO = 0;
    ArrayList<String> tipoDocumento = new ArrayList<String>();

    SQLiteDatabase db;
    Uri outputUri;
    File fileFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inseri_imagem_documento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        numeroCheck = sharedpreferences.getString("numeroCheck", null);

        if(numeroCheck==null){
            numeroCheck="0";
        }
        spnNomeDocumentos = (Spinner) findViewById(R.id.spnNomeDocumentos);

        tipoDocumento.add("Selecione um Documento");
        tipoDocumento.add("Autorização de Passagem");
        tipoDocumento.add("Cadastro de Cliente");
        tipoDocumento.add("Carta de Apresentação do Projeto");
        tipoDocumento.add("Croqui");
        tipoDocumento.add("Formulário de Acompanhamento");
        tipoDocumento.add("Licenciamento Ambiental");
        tipoDocumento.add("Termo de Garantia");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, tipoDocumento);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNomeDocumentos.setAdapter(adapter);

        spnNomeDocumentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nomeDocumento = parent.getItemAtPosition(position).toString();

                if(nomeDocumento.equals("Croqui") && !numeroCheck.equals("1")){

                    final Dialog dialog = new Dialog(InseriImagemDocumento.this);
                    dialog.setContentView(R.layout.radiochecklist);
                    dialog.setTitle("Descrição do ponto");
                    dialog.setCancelable(false);

                    Button dialogButton = (Button) dialog.findViewById(R.id.alertOK);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String valor1 = "", valor2 = "", valor3 = "", valor4 = "", valor5 = "", valor6 = "", valor7 = "", valor8 = "", valor9 = "", valor10 = "", valor11 = "", valor12 = "", valor13 = "", valor14 = "", valor15 = "";

                            RadioGroup check1 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList1);
                            RadioGroup check2 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList2);
                            RadioGroup check3 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList3);
                            RadioGroup check4 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList4);
                            RadioGroup check5 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList5);
                            RadioGroup check6 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList6);
                            RadioGroup check7 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList7);
                            RadioGroup check8 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList8);
                            RadioGroup check9 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList9);
                            RadioGroup check10 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList10);
                            RadioGroup check11 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList11);
                            RadioGroup check12 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList12);
                            RadioGroup check13 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList13);
                            RadioGroup check14 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList14);
                            RadioGroup check15 = (RadioGroup) dialog.findViewById(R.id.grupRadioCheckList15);

                            RadioButton rd1Sim = (RadioButton) dialog.findViewById(R.id.btCheckList1Sim);
                            RadioButton rd1NA = (RadioButton) dialog.findViewById(R.id.btCheckList1NA);
                            RadioButton rd2Sim = (RadioButton) dialog.findViewById(R.id.btCheckList2Sim);
                            RadioButton rd2NA = (RadioButton) dialog.findViewById(R.id.btCheckList2NA);
                            RadioButton rd3Sim = (RadioButton) dialog.findViewById(R.id.btCheckList3Sim);
                            RadioButton rd3NA = (RadioButton) dialog.findViewById(R.id.btCheckList3NA);
                            RadioButton rd4Sim = (RadioButton) dialog.findViewById(R.id.btCheckList4Sim);
                            RadioButton rd4NA = (RadioButton) dialog.findViewById(R.id.btCheckList4NA);
                            RadioButton rd5Sim = (RadioButton) dialog.findViewById(R.id.btCheckList5Sim);
                            RadioButton rd5NA = (RadioButton) dialog.findViewById(R.id.btCheckList5NA);
                            RadioButton rd6Sim = (RadioButton) dialog.findViewById(R.id.btCheckList6Sim);
                            RadioButton rd6NA = (RadioButton) dialog.findViewById(R.id.btCheckList6NA);
                            RadioButton rd7Sim = (RadioButton) dialog.findViewById(R.id.btCheckList7Sim);
                            RadioButton rd7NA = (RadioButton) dialog.findViewById(R.id.btCheckList7NA);
                            RadioButton rd8Sim = (RadioButton) dialog.findViewById(R.id.btCheckList8Sim);
                            RadioButton rd8NA = (RadioButton) dialog.findViewById(R.id.btCheckList8NA);
                            RadioButton rd9Sim = (RadioButton) dialog.findViewById(R.id.btCheckList9Sim);
                            RadioButton rd9NA = (RadioButton) dialog.findViewById(R.id.btCheckList9NA);
                            RadioButton rd10Sim = (RadioButton) dialog.findViewById(R.id.btCheckList10Sim);
                            RadioButton rd10NA = (RadioButton) dialog.findViewById(R.id.btCheckList10NA);
                            RadioButton rd11Sim = (RadioButton) dialog.findViewById(R.id.btCheckList11Sim);
                            RadioButton rd11NA = (RadioButton) dialog.findViewById(R.id.btCheckList11NA);
                            RadioButton rd12Sim = (RadioButton) dialog.findViewById(R.id.btCheckList12Sim);
                            RadioButton rd12NA = (RadioButton) dialog.findViewById(R.id.btCheckList12NA);
                            RadioButton rd13Sim = (RadioButton) dialog.findViewById(R.id.btCheckList13Sim);
                            RadioButton rd13NA = (RadioButton) dialog.findViewById(R.id.btCheckList13NA);
                            RadioButton rd14Sim = (RadioButton) dialog.findViewById(R.id.btCheckList14Sim);
                            RadioButton rd14NA = (RadioButton) dialog.findViewById(R.id.btCheckList14NA);
                            RadioButton rd15Sim = (RadioButton) dialog.findViewById(R.id.btCheckList15Sim);
                            RadioButton rd15NA = (RadioButton) dialog.findViewById(R.id.btCheckList15NA);

                            int selectedIdCheck1 = check1.getCheckedRadioButtonId();
                            if (selectedIdCheck1 == rd1Sim.getId()) {
                                valor1 = "1";
                            } else if (selectedIdCheck1 == rd1NA.getId()) {
                                valor1 = "1";
                            }

                            int selectedIdCheck2 = check2.getCheckedRadioButtonId();
                            if (selectedIdCheck2 == rd2Sim.getId()) {
                                valor2 = "1";
                            } else if (selectedIdCheck2 == rd2NA.getId()) {
                                valor2 = "1";
                            }

                            int selectedIdCheck3 = check3.getCheckedRadioButtonId();
                            if (selectedIdCheck3 == rd3Sim.getId()) {
                                valor3 = "1";
                            } else if (selectedIdCheck3 == rd3NA.getId()) {
                                valor3 = "1";
                            }

                            int selectedIdCheck4 = check4.getCheckedRadioButtonId();
                            if (selectedIdCheck4 == rd4Sim.getId()) {
                                valor4 = "1";
                            } else if (selectedIdCheck4 == rd4NA.getId()) {
                                valor4 = "1";
                            }

                            int selectedIdCheck5 = check5.getCheckedRadioButtonId();
                            if (selectedIdCheck5 == rd5Sim.getId()) {
                                valor5 = "1";
                            } else if (selectedIdCheck5 == rd5NA.getId()) {
                                valor5 = "1";
                            }

                            int selectedIdCheck6 = check6.getCheckedRadioButtonId();
                            if (selectedIdCheck6 == rd6Sim.getId()) {
                                valor6 = "1";
                            } else if (selectedIdCheck6 == rd6NA.getId()) {
                                valor6 = "1";
                            }

                            int selectedIdCheck7 = check7.getCheckedRadioButtonId();
                            if (selectedIdCheck7 == rd7Sim.getId()) {
                                valor7 = "1";
                            } else if (selectedIdCheck7 == rd7NA.getId()) {
                                valor7 = "1";
                            }

                            int selectedIdCheck8 = check8.getCheckedRadioButtonId();
                            if (selectedIdCheck8 == rd8Sim.getId()) {
                                valor8 = "1";
                            } else if (selectedIdCheck8 == rd8NA.getId()) {
                                valor8 = "1";
                            }

                            int selectedIdCheck9 = check9.getCheckedRadioButtonId();
                            if (selectedIdCheck9 == rd9Sim.getId()) {
                                valor9 = "1";
                            } else if (selectedIdCheck9 == rd9NA.getId()) {
                                valor9 = "1";
                            }

                            int selectedIdCheck10 = check10.getCheckedRadioButtonId();
                            if (selectedIdCheck10 == rd10Sim.getId()) {
                                valor10 = "1";
                            } else if (selectedIdCheck10 == rd10NA.getId()) {
                                valor10 = "1";
                            }

                            int selectedIdCheck11 = check11.getCheckedRadioButtonId();
                            if (selectedIdCheck11 == rd11Sim.getId()) {
                                valor11 = "1";
                            } else if (selectedIdCheck11 == rd11NA.getId()) {
                                valor11 = "1";
                            }

                            int selectedIdCheck12 = check12.getCheckedRadioButtonId();
                            if (selectedIdCheck12 == rd12Sim.getId()) {
                                valor12 = "1";
                            } else if (selectedIdCheck12 == rd12NA.getId()) {
                                valor12 = "1";
                            }

                            int selectedIdCheck13 = check13.getCheckedRadioButtonId();
                            if (selectedIdCheck13 == rd13Sim.getId()) {
                                valor13 = "1";
                            } else if (selectedIdCheck13 == rd13NA.getId()) {
                                valor13 = "1";
                            }

                            int selectedIdCheck14 = check14.getCheckedRadioButtonId();
                            if (selectedIdCheck14 == rd14Sim.getId()) {
                                valor14 = "1";
                            } else if (selectedIdCheck14 == rd14NA.getId()) {
                                valor14 = "1";
                            }

                            int selectedIdCheck15 = check15.getCheckedRadioButtonId();
                            if (selectedIdCheck15 == rd15Sim.getId()) {
                                valor15 = "1";
                            } else if (selectedIdCheck15 == rd15NA.getId()) {
                                valor15 = "1";
                            }
/*
                            Log.d("teste","1: "+valor1);
                            Log.d("teste","2: "+valor2);
                            Log.d("teste","3: "+valor3);
                            Log.d("teste","4: "+valor4);
                            Log.d("teste","5: "+valor5);
                            Log.d("teste","6: "+valor6);
                            Log.d("teste","7: "+valor7);
                            Log.d("teste","8: "+valor8);
                            Log.d("teste","9: "+valor9);
                            Log.d("teste","10: "+valor10);
                            Log.d("teste","11: "+valor11);
                            Log.d("teste","12: "+valor12);
                            Log.d("teste","13: "+valor13);
                            Log.d("teste","14: "+valor14);
                            Log.d("teste","15: "+valor15);
*/
                            if(valor1.equals("1") && valor2.equals("1") && valor3.equals("1") && valor4.equals("1") && valor5.equals("1") && valor6.equals("1")&& valor7.equals("1") && valor8.equals("1") && valor9.equals("1") && valor10.equals("1") && valor11.equals("1") && valor12.equals("1") && valor13.equals("1")&& valor14.equals("1") && valor15.equals("1")){
                                SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("numeroCheck", "1");
                                editor.commit();
                                dialog.dismiss();
                            }else{
                                Toast.makeText(InseriImagemDocumento.this, "Todos os campos são obrigatório!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    Button dialogButtonCancela = (Button) dialog.findViewById(R.id.alertCancelar);
                    dialogButtonCancela.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            Intent intent = new Intent(InseriImagemDocumento.this, InseriImagemDocumento.class);
                            InseriImagemDocumento.this.startActivity(intent);
                            finish();

                        }
                    });
                    dialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    // Método que pega a ação do botão "Tirar Foto"
    public void foto(View v) {
        if (nomeDocumento.equals("Selecione um Documento")) { // verifica se inseriu o nome do arquivo
            Toast.makeText(InseriImagemDocumento.this, "Por Favor! Selecione o nome do Arquivo!", Toast.LENGTH_SHORT).show();

        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(InseriImagemDocumento.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                outputUri = getTempCameraUri();
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                startActivityForResult(intent, 1);
            } else {
                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (it.resolveActivity(getPackageManager()) != null) {
                    File arquivoFoto = null;
                    try {
                        arquivoFoto = criaArquivoImagem();
                    } catch (IOException ex) {

                    }
                    if (arquivoFoto != null) {
                        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(arquivoFoto));
                        startActivityForResult(it, 0);
                    }
                }
            }
        }
    }

    // Método que cria as pastas onde serão inseridos as imagens
    private File criaArquivoImagem() throws IOException {
        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        String numeroNotaSelect = sharedpreferences.getString("numeroNotaKey", null);

        String horarioSistema = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String nomeArquivoImagem = "JPEG_" + numeroNotaSelect + "_" + nomeDocumento + "_" + horarioSistema + "_";

        String nomeArquivoImagem = "Documento " + nomeDocumento + " " + numeroNotaSelect + "_" + horarioSistema;

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HCC/";

        // Verificar se a pasta HCC existe, caso não exista, crie o diretório
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        // Verificar se a pasta com o numero da nota existe, caso não exista, crie o diretório dentro da pasta HCC
        String path2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HCC/" + numeroNotaSelect;
        File dir2 = new File(path2);
        if (!dir2.exists())
            dir2.mkdirs();

        File imagem = File.createTempFile(nomeArquivoImagem, ".jpg", dir2);
        caminhoImagem = imagem.getAbsolutePath();

        return imagem;
    }

    // Método com resultado da negação da permissão
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(InseriImagemDocumento.this, "Desculpe!!! você não pode usar este aplicativo sem conceder permissão!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        ImageView iv = (ImageView) findViewById(R.id.foto);
        Bitmap bitmap = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (outputUri == null) {
                return;
            }
            if (outputUri != null) {

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputUri);
                    Bitmap tamanhoReduzidoImagem, imagemVirada;
                    tamanhoReduzidoImagem = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.2), (int) (bitmap.getHeight() * 0.2), true);

                    if (tamanhoReduzidoImagem.getWidth() > tamanhoReduzidoImagem.getHeight()) {
                        tamanhoReduzidoImagem = RotateBitmap(tamanhoReduzidoImagem, 90);
                    }
                    imgString = Base64.encodeToString(getBytesFromBitmap(tamanhoReduzidoImagem), Base64.NO_WRAP);
                    if (bitmap.getWidth() > bitmap.getHeight()) {
                        bitmap = RotateBitmap(bitmap, 90);
                    }
                    iv.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (resultCode == RESULT_OK) {
                if (requestCode == CODIGO_REQUISICAO) {

                    File arquivoImagem = new File(caminhoImagem);
                    if (arquivoImagem.exists()) {
                        Bitmap imagemBitmap = BitmapFactory.decodeFile(
                                arquivoImagem.getAbsolutePath());

                        Bitmap tamanhoReduzidoImagem, imagemVirada;
                        imagemVirada = RotateBitmap(imagemBitmap, 90);
                        tamanhoReduzidoImagem = Bitmap.createScaledBitmap(imagemVirada, (int) (imagemVirada.getWidth() * 0.2), (int) (imagemVirada.getHeight() * 0.2), true);
                        imgString = Base64.encodeToString(getBytesFromBitmap(tamanhoReduzidoImagem), Base64.NO_WRAP);
                        iv.setImageBitmap(RotateBitmap(imagemBitmap, 90));
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Você NÃO inseriu a foto!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método que gira a imagem em 90 graus
    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    // Método de codifica a imagem, para base64
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    // Método que transforma a imagem em byte[]
    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Cria local onde será salvo a imagem
    private Uri getTempCameraUri() {
        try {
            SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
            String numeroNotaSelect = sharedpreferences.getString("numeroNotaKey", null);

            // Cria o nome do arquivo com numeroDaNota+NomeDoDocumento+horarioDoSistema
            String horarioSistema = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            //String nomeArquivoImagem = "JPEG_" + numeroNotaSelect + "_" + nomeDocumento + "_" + horarioSistema + "_";
            String nomeArquivoImagem = "Documento " + nomeDocumento + " " + numeroNotaSelect + "_" + horarioSistema;

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HCC/";

            // Verificar se a pasta HCC existe, caso não exista, crie o diretório
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            // Verificar se a pasta com o numero da nota existe, caso não exista, cria o diretório dentro da pasta HCC
            String path2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/HCC/" + numeroNotaSelect;
            File dir2 = new File(path2);
            if (!dir2.exists())
                dir2.mkdirs();

            // Salva a imagem no caminho criado
            fileFinal = File.createTempFile(nomeArquivoImagem, ".jpg", dir2);
            Uri photoURI2 = FileProvider.getUriForFile(InseriImagemDocumento.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    fileFinal);

            return photoURI2;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método que pega  a ação do Botão "Salvar"
    public void salvarImagemDocumento(View v) {
        if (imgString == null) { // Verifica se a imagem foi tirada
            Toast.makeText(InseriImagemDocumento.this, "A Foto eh Obrigatória ", Toast.LENGTH_SHORT).show();
        } else {
            addImagemDocBanco();
        }
    }

    // Método que salva a imagem no banco de dados
    private void addImagemDocBanco() {
        SharedPreferences sharedpreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
        String idLocacaoSelecionada = sharedpreferences.getString("idLocacaoKey", null);

        if (nomeDocumento.equals("Selecione um Documento")) {// verifica se o nome do documento foi selecionado
            Toast.makeText(InseriImagemDocumento.this, "Por Favor! Selecione o nome do Arquivo!", Toast.LENGTH_SHORT).show();
        } else {
            db = openOrCreateDatabase(BANCO, Context.MODE_PRIVATE, null);
            // Inserção dos dados no banco
            db.execSQL("INSERT INTO " + TABELADOCUMENTO + "(NOMEDOCUMENTO, IMAGEM, IDLOCACAO) VALUES ('" + nomeDocumento + "','" + imgString + "','" + idLocacaoSelecionada + "')");
            db.close();
            Toast.makeText(getApplicationContext(), "Inserção realizada com sucesso!", Toast.LENGTH_SHORT).show();

            Intent it;
            it = new Intent(this, GerenciarLocacoes.class);
            startActivity(it);
            finish();
        }
    }

    //Metodo responsavel por pegar ação do botão nativo "Voltar" do smartfone
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, GerenciarLocacoes.class);
        this.startActivity(intent);
        finish();
        super.onBackPressed();
    }

}
