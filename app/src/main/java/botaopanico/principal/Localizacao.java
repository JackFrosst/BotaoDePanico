package botaopanico.principal;

public class Localizacao {
    private String latitude;
    private String longitude;
    private String nomeRementente;
    private String codAlerta;

    public String getCodAlerta() {
        return codAlerta;
    }

    public void setCodAlerta(String codAlerta) {
        this.codAlerta = codAlerta;
    }

    public String getNomeRementente() {
        return nomeRementente;
    }

    public void setNomeRementente(String nomeRementente) {
        this.nomeRementente = nomeRementente;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return nomeRementente + "\n"
                + "Clique para verificar a localização";
    }
}
