package ma.dentalTech.service.modules.dossierMedicale.impl;

import ma.dentalTech.service.modules.dossierMedicale.api.CertificatService;
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
    public Certificat create(Certificat c) {
        try {
            certificatRepository.create(c);
            return c;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Certificat update(Long id, Certificat c) {
        try {
            c.setIdCertificat(id);
            certificatRepository.update(c);
            return c;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Certificat delete(Certificat c) {
        try {
            certificatRepository.delete(c);
            return c;
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

