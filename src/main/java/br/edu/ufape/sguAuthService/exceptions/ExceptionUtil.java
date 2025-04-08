package br.edu.ufape.sguAuthService.exceptions;

import br.edu.ufape.sguAuthService.exceptions.uniqueConstraintViolationException.UniqueConstraintViolationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

public class ExceptionUtil {

    public static RuntimeException handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Throwable cause = e.getCause();
            if (cause instanceof ConstraintViolationException constraintViolationException) {
                String message= constraintViolationException.getMessage();
                if(message.contains("usuario_cpf_key")){throw new UniqueConstraintViolationException("cpf", "CPF já cadastrado!");}
                else if (message.contains("usuario_email_key")){throw new UniqueConstraintViolationException("email", "Email já cadastrado!");}
                else if (message.contains("aluno_matricula_key")){throw new UniqueConstraintViolationException("matricula", "Matrícula já cadastrada!");}
                else if (message.contains("curso_nome_key")){throw new UniqueConstraintViolationException("curso", "Curso já cadastrado!");}
                else if (message.contains("tipo_unidade_administrativa_nome_key"))
                {throw new UniqueConstraintViolationException("tipo unidade administrativa", "Tipo de unidade administrativa já cadastrado!");}
                else if (message.contains("unidade_administrativa_nome_key")){throw new UniqueConstraintViolationException("Unidade Administrativa", "Nome de Unidade já cadastrada!");}
                else if (message.contains("unidade_administrativa_codigo_key")){throw new UniqueConstraintViolationException("Unidade Administrativa", "Código de Unidade já cadastrada!");}
                else throw new UniqueConstraintViolationException("desconhecido", "Violação de restrição única desconhecida");

                }
        return new IllegalArgumentException("Erro de integridade de dados!");
    }

}
