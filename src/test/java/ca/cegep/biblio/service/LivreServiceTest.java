package ca.cegep.biblio.service;

import ca.cegep.biblio.model.Livre;
import ca.cegep.biblio.repository.LivreRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TP partie C : à vous d'implémenter les 3 tests ci-dessous.
 * Retirez @Disabled de chaque test une fois son implémentation écrite.
 * Respectez le modèle Arrange / Act / Assert.
 */
@ExtendWith(MockitoExtension.class)
class LivreServiceTest {

    @Mock
    private LivreRepository repository;

    @InjectMocks
    private LivreService service;

    // ---------- 1 test simple (sans mock) ----------

    @Test
    void calculerFraisRetard_longRetard_plafonneLeMontant() {
        // À tester : pour une catégorie donnée, un très grand nombre de jours de retard
        // ne dépasse jamais le plafond de cette catégorie.
        // Indice : la méthode ne dépend pas du repository.

        // Arrange
        Livre nouveau = new Livre(null, "Dune", "Frank Herbert", "9782266320481", 1965, "ROMAN");

        // Act
        double montantFrais = service.calculerFraisRetard("JEUNESSE", 1000);

        // Assert
        assertTrue(montantFrais <= 5.00);
        assertEquals(5.00, montantFrais);
    }

    // ---------- 1 test sur une exception (assertThrows) ----------

    @Test
    void emprunter_livreDejaEmprunte_lanceIllegalStateException() {
        // À tester : emprunter un livre dont disponible == false lance une
        // IllegalStateException.
        // Indice : le repository (mock) doit retourner ce livre pour son id.

        // Arrange
        Livre livre = new Livre(10L, "Prout", "Frank rrrrr", "3938998538", 2000, "ROMAN");
        livre.setDisponible(false);

        // Act
        when(repository.findById(10L)).thenReturn(Optional.of(livre));

        // Assert
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.emprunter(10L));
        assertNotNull(exception.getMessage());
    }

    // ---------- 1 test avec mock (when / thenReturn / verify) ----------

    @Test
    void emprunter_livreDisponible_marqueIndisponibleEtSauvegarde() {
        // À tester : emprunter un livre disponible le rend indisponible
        // et appelle repository.save(...) exactement une fois.
        // Définissez le comportement du mock avec when(...).thenReturn(...).
        
        // Arrange
        Livre nouveau = new Livre(10L, "Dune", "Frank Herbert", "9782266320481", 1965, "ROMAN");
        when(repository.findById(10L)).thenReturn(Optional.of(nouveau));
        
        // Act
        service.emprunter(10L);
        
        // Assert
        verify(repository).save(nouveau);

    }
}
