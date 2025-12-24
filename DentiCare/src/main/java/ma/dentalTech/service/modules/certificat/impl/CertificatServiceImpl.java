package ma.dentalTech.service.modules.certificat.impl;

import ma.dentalTech.service.modules.certificat.api.CertificatService;
import ma.dentalTech.repository.modules.dossierMedicale.api.CertificatRepo;
import ma.dentalTech.entities.Certificat.Certificat;
import ma.dentalTech.conf.ApplicationContext;
import java.util.List;
import java.util.Optional;

public class CertificatServiceImpl implements CertificatService {
    
    private final CertificatRepo certificatRepository;
    
    public CertificatServiceImpl() {

        this.certificatRepository = ApplicationContext.getBean(CertificatRepo.class);
    }
    
    @Override
    public List<Certificat> findAll() throws Exception {
        return certificatRepository.findAll();
    }

    @Override
    public Optional<Certificat> findByID(Long id) throws Exception {
        return certificatRepository.findById(id);
    }

    @Override
    public Certificat create(Certificat item) {
        try {
            certificatRepository.create(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Certificat update(Long id, Certificat item) {
        try {
            item.setIdCertificat(id);
            certificatRepository.update(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Certificat delete(Certificat item) {
        try {
            certificatRepository.delete(item);
            return item;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteByID(Long id) {
        try {
            certificatRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}

