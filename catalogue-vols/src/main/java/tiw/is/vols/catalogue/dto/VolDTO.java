package tiw.is.vols.catalogue.dto;

public record VolDTO(String id, String companie, boolean isDepart, String pointLivraisonBagages) {


    @Override
    public String id() {
        return id;
    }

    @Override
    public String companie() {
        return companie;
    }

    @Override
    public boolean isDepart() {
        return isDepart;
    }

    @Override
    public String pointLivraisonBagages() {
        return pointLivraisonBagages;
    }
}
