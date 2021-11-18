package botaopanico.principal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BdSqLiteCadastroLogin extends SQLiteOpenHelper {
    // variaveis para criação de banco e tabela de remetente
    private static final String NOME_BANCO = "RemetenteDestinatario.db";
    private static final int VERSAO = 8;
    private static final String TABELAREM = "tbRementente";
    private static final String ID = "id";
    private static final String NOME = "nome";
    private static final String SEGUNDONOME = "segundoNome";
    private static final String NUMEROTELEFONE = "numeroTelefone";
    // variaveis para a criação da tabela de destinatários
    private static final String TABELADEST = "tbDestinatario";
    private static final String ID_DEST = "id";
    private static final String DESTINATARIO = "numDestinatario";
    private static final String NOMEDEST = "nomeDestinatario";
    // variavies para a criação da tabela notificação
    private static final String TABELALOCAL = "tbLocalizacao";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String CODALERTA = "codAlerta";


    public BdSqLiteCadastroLogin(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //cria se não existir a tabela de remetente
        String sqlRementente = "CREATE TABLE IF NOT EXISTS " + TABELAREM + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NOME + " TEXT," +
                SEGUNDONOME + " TEXT," +
                NUMEROTELEFONE + " TEXT);";

        db.execSQL(sqlRementente);
        //cria se não existir a tabela de destinatario
        String sqlDestinatario = "CREATE TABLE IF NOT EXISTS " + TABELADEST + " (" +
                ID_DEST + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DESTINATARIO + " TEXT," +
                NOMEDEST + " TEXT);";

        db.execSQL(sqlDestinatario);
        String sqlLocalizacao = "CREATE TABLE IF NOT EXISTS " + TABELALOCAL + " (" +
                LATITUDE + " TEXT," +
                LONGITUDE + " TEXT," +
                NOME + " TEXT," +
                CODALERTA + " TEXT);";

        db.execSQL(sqlLocalizacao);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sqlRem = "DROP TABLE IF EXISTS " + TABELAREM;
        db.execSQL(sqlRem);
        onCreate(db);
        String sqlDest = "DROP TABLE IF EXISTS " + TABELADEST;
        db.execSQL(sqlDest);
        onCreate(db);
    }
    // ========== metodos para o cadastrar,alterar,excluir e consultar a tabela REMETENTE =========
    public long cadastrarRemetente(Remente remente) {
        ContentValues valores = new ContentValues();
        long retornoDB;

        valores.put(NOME, remente.getPrimeiroNome());
        valores.put(SEGUNDONOME, remente.getSegundoNome());
        valores.put(NUMEROTELEFONE, remente.getNumeroCelular());

        retornoDB = getWritableDatabase().insert(TABELAREM, null, valores);
        return retornoDB;
    }

    public long alterarRemetente(Remente remente) {
        ContentValues valores = new ContentValues();
        long retornoDB;

        valores.put(NOME, remente.getPrimeiroNome());
        valores.put(SEGUNDONOME, remente.getSegundoNome());
        valores.put(NUMEROTELEFONE, remente.getNumeroCelular());


        String[] args = {String.valueOf(remente.getId())};
        retornoDB = getWritableDatabase().update(TABELAREM, valores, "id=?", args);
        return retornoDB;
    }

    public long excluirRemetente(Remente remente) {
        long retornoDB;

        String[] args = {String.valueOf(remente.getId())};
        retornoDB = getWritableDatabase().delete(TABELAREM, ID + "=?", args);
        return retornoDB;
    }

    public ArrayList<Remente> consultarRemetente() {

        String[] colunas = {ID, NOME, SEGUNDONOME, NUMEROTELEFONE};

        Cursor cursor = getWritableDatabase().query(TABELAREM, colunas, null,
                null, null, null,
                "upper(nome)", null);

        ArrayList<Remente> listaRementente = new ArrayList<>();

        Remente remente;

        while (cursor.moveToNext()) {
            remente = new Remente();

            remente.setId(cursor.getInt(0));
            remente.setPrimeiroNome(cursor.getString(1));
            remente.setSegundoNome(cursor.getString(2));
            remente.setNumeroCelular(cursor.getString(3));

            listaRementente.add(remente);
        }
        return listaRementente;
    }

    public int consultarLoginRemetente() {

        String[] colunas = {ID, NOME, SEGUNDONOME, NUMEROTELEFONE};

        Cursor cursor = getWritableDatabase().query(TABELAREM, colunas, null,
                null, null, null,
                "upper(nome)", null);

        if(cursor.getCount() >= 1){
            return 1;
        }

        return 0;
    }

    // ========== metodos para o cadastrar,alterar,excluir e consultar a tabela DESTINATARIO =========

    public long cadastrarDestinatario(Destinatario destinatario) {
        ContentValues valores = new ContentValues();
        long retornoDB;

        valores.put(DESTINATARIO, destinatario.getNumeroCelular());
        valores.put(NOMEDEST, destinatario.getNomeDest());

        retornoDB = getWritableDatabase().insert(TABELADEST, null, valores);
        return retornoDB;
    }

    public ArrayList<Destinatario> consultarDestinatario() {

        String[] colunas = {ID_DEST, DESTINATARIO, NOMEDEST};

        Cursor cursor = getWritableDatabase().query(TABELADEST, colunas, null,
                null, null, null,
                 null);

        ArrayList<Destinatario> listaDestinatario = new ArrayList<>();

        Destinatario destinatario;

        while (cursor.moveToNext()) {
            destinatario = new Destinatario();

            destinatario.setId(cursor.getInt(0));
            destinatario.setNumeroCelular(cursor.getString(1));
            destinatario.setNomeDest(cursor.getString(2));
            listaDestinatario.add(destinatario);
        }

        return listaDestinatario;
    }

    public long excluirDestinatario(Destinatario destinatario) {
        long retornoDB;
        String[] args = {String.valueOf(destinatario.getId())};
        retornoDB = getWritableDatabase().delete(TABELADEST, ID_DEST + "=?", args);
        return retornoDB;
    }

   // ============== metodos para o cadastrar,alterar,excluir e consultar a tabela LOCALIZACAO =====

    public long cadastrarLocalizacao(Localizacao localizacao) {
        ContentValues valores = new ContentValues();
        long retornoDB;

        valores.put(LATITUDE, localizacao.getLatitude());
        valores.put(LONGITUDE, localizacao.getLongitude());
        valores.put(NOME, localizacao.getNomeRementente());
        valores.put(CODALERTA, localizacao.getCodAlerta());

        retornoDB = getWritableDatabase().insert(TABELALOCAL, null, valores);
        return retornoDB;
    }

    public ArrayList<Localizacao> consultarLocalizacao() {

        String[] colunas = {LATITUDE, LONGITUDE, NOME, CODALERTA};

        Cursor cursor = getWritableDatabase().query(TABELALOCAL, colunas, null,
                null, null, null,
                null);

        ArrayList<Localizacao> listaLocalizacao = new ArrayList<>();

        Localizacao localizacao;

        while (cursor.moveToNext()) {
            localizacao = new Localizacao();

            localizacao.setLatitude(cursor.getString(0));
            localizacao.setLongitude(cursor.getString(1));
            localizacao.setNomeRementente(cursor.getString(2));
            localizacao.setCodAlerta(cursor.getString(3));
            listaLocalizacao.add(localizacao);
        }

        return listaLocalizacao;
    }

    public long excluirLocalizacao(Localizacao localizacao) {
        long retornoDB;
        String[] args = {String.valueOf(localizacao.getCodAlerta())};
        retornoDB = getWritableDatabase().delete(TABELALOCAL, CODALERTA + "=?", args);
        return retornoDB;
    }


}
