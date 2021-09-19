package botaopanico.principal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class BdSqLiteCadastroLogin extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "RemetenteDestinatario.db";
    private static final int VERSAO = 1;
    private static final String TABELAREM = "tbRementente";
    private static final String TABELADEST = "tbDestinatario";
    private static final String ID = "id";
    private static final String NOME = "nome";
    private static final String SEGUNDONOME = "segundoNome";
    private static final String NUMEROTELEFONE = "numeroTelefone";


    public BdSqLiteCadastroLogin(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABELAREM + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NOME + " TEXT," +
                SEGUNDONOME + " TEXT," +
                NUMEROTELEFONE + " TEXT);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABELAREM;
        db.execSQL(sql);
        onCreate(db);
    }

    public long cadastrar(RementeDestinatario rementeDestinatario) {
        ContentValues valores = new ContentValues();
        long retornoDB;

        valores.put(NOME, rementeDestinatario.getPrimeiroNome());
        valores.put(SEGUNDONOME, rementeDestinatario.getSegundoNome());
        valores.put(NUMEROTELEFONE, rementeDestinatario.getNumeroCelular());

        retornoDB = getWritableDatabase().insert(TABELAREM, null, valores);
        return retornoDB;
    }

    public long alterar(RementeDestinatario rementeDestinatario) {
        ContentValues valores = new ContentValues();
        long retornoDB;

        valores.put(NOME, rementeDestinatario.getPrimeiroNome());
        valores.put(SEGUNDONOME, rementeDestinatario.getSegundoNome());
        valores.put(NUMEROTELEFONE, rementeDestinatario.getNumeroCelular());


        String[] args = {String.valueOf(rementeDestinatario.getId())};
        retornoDB = getWritableDatabase().update(TABELAREM, valores, "id=?", args);
        return retornoDB;
    }

    public long excluir(RementeDestinatario rementeDestinatario) {
        long retornoDB;

        String[] args = {String.valueOf(rementeDestinatario.getId())};
        retornoDB = getWritableDatabase().delete(TABELAREM, ID + "=?", args);
        return retornoDB;
    }

    public ArrayList<RementeDestinatario> consultar() {

        String[] colunas = {ID, NOME, SEGUNDONOME, NUMEROTELEFONE};

        Cursor cursor = getWritableDatabase().query(TABELAREM, colunas, null,
                null, null, null,
                "upper(nome)", null);

        ArrayList<RementeDestinatario> listaRementente = new ArrayList<>();

        RementeDestinatario rementeDestinatario;

        while (cursor.moveToNext()) {
            rementeDestinatario = new RementeDestinatario();

            rementeDestinatario.setId(cursor.getInt(0));
            rementeDestinatario.setPrimeiroNome(cursor.getString(1));
            rementeDestinatario.setSegundoNome(cursor.getString(2));
            rementeDestinatario.setNumeroCelular(cursor.getString(3));

            listaRementente.add(rementeDestinatario);
        }
        return listaRementente;
    }

}
