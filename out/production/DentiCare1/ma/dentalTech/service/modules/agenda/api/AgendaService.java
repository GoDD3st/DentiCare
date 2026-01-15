package ma.dentalTech.service.modules.agenda.api;

import ma.dentalTech.entities.AgendaMensuel.AgendaMensuel;
import ma.dentalTech.service.common.MainService;

/**
 * API pour la gestion du planning mensuel du médecin (Module 5).
 */
public interface AgendaService extends MainService<AgendaMensuel, Long> {
}