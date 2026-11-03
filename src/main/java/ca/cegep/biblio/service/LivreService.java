package ca.cegep.biblio.service;

import ca.cegep.biblio.exception.LivreIntrouvableException;
import ca.cegep.biblio.model.Livre;
import ca.cegep.biblio.repository.LivreRepository;
import org.springframework.stereotype.Service;
import ca.cegep.biblio.model.Categorie;
import java.util.List;

/** Logique métier de la bibliothèque. */
@Service
public class LivreService {

    private final LivreRepository repository;

    public LivreService(LivreRepository repository) {
        this.repository = repository;
    }

    public List<Livre> lister() {
        return repository.findAll();
    }

    public Livre trouver(Long id) {
        return repository.findById(id).orElseThrow(() -> new LivreIntrouvableException(id));
    }

    public Livre creer(String titre, String auteur, String isbn, int anneePublication, Categorie categorie) {
        validation(titre, auteur, anneePublication);
        Livre livre = new Livre(null, titre, auteur, isbn, anneePublication, categorie);
        return repository.save(livre);
    }

    public Livre modifier(Long id, String titre, String auteur, String isbn, int anneePublication, Categorie categorie) {
        Livre livre = trouver(id);
        validation(titre, auteur, anneePublication);
        livre.setTitre(titre);
        livre.setAuteur(auteur);
        livre.setIsbn(isbn);
        livre.setAnneePublication(anneePublication);
        livre.setCategorie(categorie);
        return repository.save(livre);
    }

    public void supprimer(Long id) {
        trouver(id);
        repository.deleteById(id);
    }

    public Livre emprunter(Long id) {
        Livre livre = trouver(id);
        if (!livre.isDisponible()) {
            throw new IllegalStateException("Ce livre est déjà emprunté");
        }
        livre.setDisponible(false);
        return repository.save(livre);
    }

    public Livre retourner(Long id) {
        Livre livre = trouver(id);
        if (livre.isDisponible()) {
            throw new IllegalStateException("Ce livre n'est pas emprunté");
        }
        livre.setDisponible(true);
        return repository.save(livre);
    }

    public double calculerFraisRetard(Categorie categorie, int joursRetard) {
        return categorie.calculerFraisRetard(joursRetard);
    }
    
    public void validation(String titre, String auteur, int anneePublication) {
        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }
        if (auteur == null || auteur.isBlank()) {
            throw new IllegalArgumentException("L'auteur est obligatoire");
        }
        if (anneePublication < 1450 || anneePublication > 2100) {
            throw new IllegalArgumentException("L'année de publication est invalide");
        }
    }
}
