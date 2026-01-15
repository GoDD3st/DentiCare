package ma.dentalTech.service.modules.auth.api;

import java.util.Map;
import ma.dentalTech.mvc.dto.authentificationDtos.AuthRequest;

public interface LoginFormValidator {
    Map<String, String> validate(AuthRequest request);
}
