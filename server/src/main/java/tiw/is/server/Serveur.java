package tiw.is.server;

import java.util.Map;


/*
todo:
 1- Créer des company (à l'initialisation)
 2- Créer un vol à l'arrivée
 3- Ajouter un bagage à un vol
 4- Indiquer la livraison d'un bagage
 5- Indiquer la récupération d'un bagage
 6- Indiquer la fermeture de la livraison des bagages pour un vol
 7- Lister les bagages perdus (non livrés)
 8- Lister les bagages non récupérés
*/

public interface Serveur {
    public Object processRequest(String command, Map<String, Object> params);
}

