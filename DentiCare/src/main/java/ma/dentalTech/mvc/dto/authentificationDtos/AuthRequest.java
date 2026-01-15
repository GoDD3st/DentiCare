package ma.dentalTech.mvc.dto.authentificationDtos;


public record AuthRequest(
        String login,
        String password
) {}
