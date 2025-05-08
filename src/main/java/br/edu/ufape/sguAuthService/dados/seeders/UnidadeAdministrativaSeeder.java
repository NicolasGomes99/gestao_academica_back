package  br.edu.ufape.sguAuthService.dados.seeders;


import br.edu.ufape.sguAuthService.dados.TipoUnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.dados.UnidadeAdministrativaRepository;
import br.edu.ufape.sguAuthService.models.TipoUnidadeAdministrativa;
import br.edu.ufape.sguAuthService.models.UnidadeAdministrativa;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component @RequiredArgsConstructor
public class UnidadeAdministrativaSeeder {

    private static final List<String> ESTRUTURA = Arrays.asList(
            // Reitoria
            "Reitoria - REIT - Reitoria",
            "Gabinete da Reitoria - GAB.REIT - Gabinete",
            "Secretaria da Reitoria - SEC.REIT - Secretaria",
            "Assessoria de Gestão - ASG.REIT - Assessoria",
            "Assessoria de Relações Públicas - ASRP.REIT - Assessoria",
            "Vice-Reitoria - VREIT.REIT - Vice-Reitoria",
            "Secretaria da Vice-Reitoria - SEC.VREIT - Secretaria",
            "Secretaria Geral dos Conselhos - SECGC.REIT - Secretaria",
            "Secretaria de Acessibilidade - SECAC.REIT - Secretaria",
            "Seção de Apoio Administrativo - SAA.SECAC - Seção",
            "Secretaria de Cerimonial - SECC.REIT - Secretaria",
            "Diretoria de Comunicação - DCOMU.REIT - Diretoria",
            "Secretaria da Diretoria de Comunicação - SEC.DCOMU - Secretaria",
            "Seção de Comunicação Institucional - SCOMUINS.DCOMU - Seção",
            "Seção de Comunicação Interinstitucional - SCOMUINT.DCOMU - Seção",
            "Diretoria de Relações Internacionais - DRI.REIT - Diretoria",
            "Secretaria da Diretoria de Relações Internacionais - SEC.DRI - Secretaria",
            "Coordenadoria de Mobilidade e Administração - CMA.DRI - Coordenadoria",
            "Seção de Línguas Estrangeiras - SLE.DRI - Seção",
            "Diretoria de Relações Interinstitucionais - DRINT.REIT - Diretoria",
            "Secretaria da Diretoria de Relações Interinstitucionais - SEC.DRINT - Secretaria",
            "Coordenadoria de Celebração de Parcerias - CCP.DRINT - Coordenadoria",
            "Procuradoria Jurídica - PJ.REIT - Procuradoria",
            "Seção de Apoio Jurídico - SAJ.PJ - Seção",
            "Ouvidoria - OUV.REIT - Ouvidoria",
            "Secretaria da Ouvidoria - SEC.OUV - Secretaria",
            "Auditoria Interna - AUDIN.REIT - Auditoria",
            "Secretaria da Auditoria Interna - SEC.AUDIN - Secretaria",

            // Pró-Reitoria de Administração
            "Pró-Reitoria de Administração - PROAD.REIT - Pró-Reitoria",
            "Secretaria da Pró-Reitoria de Administração - SEC.PROAD - Secretaria",
            "Seção de Apoio Jurídico - SAJ.PROAD - Seção",
            "Seção de Concessão de Diárias e Passagens - SCDP.PROAD - Seção",
            "Secretaria de Protocolo, Comunicação e Distribuição - SPCD.PROAD - Secretaria",
            "Departamento de Patrimônio e Almoxarifado - DPA.PROAD - Departamento",
            "Coordenadoria de Patrimônio - CPAT.DPA - Coordenadoria",
            "Coordenadoria de Almoxarifado - CALM.DPA - Coordenadoria",
            "Departamento de Contabilidade e Finanças - DCF.PROAD - Departamento",
            "Coordenadoria de Contabilidade - CCO.DCF - Coordenadoria",
            "Coordenadoria de Execução Financeira - CEF.DCF - Coordenadoria",
            "Departamento de Compras - DCOMPROAD - Departamento",
            "Coordenadoria de Planejamento e Aquisição - CPIA.DCOM - Coordenadoria",
            "Coordenadoria de Licitação - CLIDCOM - Coordenadoria",
            "Departamento de Gestão Orçamentária - DGO.PROAD - Departamento",
            "Seção de Acompanhamento e Controle Orçamentário - SACO.DGO - Seção",
            "Seção de Planejamento e Análise Orçamentária - SPLO.DGO - Seção",
            "Prefeitura - PREF.PROAD - Prefeitura",
            "Secretaria da Prefeitura - SEC.PREF - Secretaria",
            "Secretaria de Meio Ambiente e Sustentabilidade - SMAS.PREF - Secretaria",
            "Coordenadoria de Contratos - CC.PREF - Coordenadoria",
            "Seção de Fiscalização e Gestão de Contratos - SFGCCC - Seção",
            "Seção de Acompanhamento de Contratos - SAC.CC - Seção",
            "Departamento de Transporte - DTRAN.PREF - Departamento",
            "Departamento de Engenharia e Arquitetura - DEA.PREF - Departamento",
            "Secretaria de Assuntos Específicos - SAE.DEA - Secretaria",
            "Coordenadoria de Manutenção - CM.DEA - Coordenadoria",
            "Seção de Manutenção Predial - SMP.CM - Seção",
            "Seção de Manutenção de Máquinas e Equipamentos - SMME.CM - Seção",
            "Coordenadoria de Projetos - CP.DEA - Coordenadoria",
            "Seção de Projetos - SPRO.CP - Seção",
            "Seção de Compatibilização de Projetos - SCPRO.CP - Seção",
            "Coordenadoria de Perícias e Orçamentos - CPO.DEA - Coordenadoria",
            "Seção de Perícias e Avaliações - SPA.CPO - Seção",
            "Seção de Orçamentos - SO.CPO - Seção",

            // Pró-Reitoria de Planejamento
            "Pró-Reitoria de Planejamento - PROPLAN.REIT - Pró-Reitoria",
            "Secretaria da Pró-Reitoria de Planejamento - SEC.PROPLAN - Secretaria",
            "Departamento de Planejamento, Informações Institucionais e Captação de Recursos - DPLIC.PROPLAN - Departamento",
            "Coordenadoria de Informações e Avaliações Institucionais - CIAI.DPLIC - Coordenadoria",
            "Seção de Documentação e Gerenciamento de Informações - SDGIN.CIAI - Seção",
            "Seção de Monitoramento e Avaliação Institucional - SMAIN.CIAI - Seção",
            "Coordenadoria de Planejamento - CPLA.DPLIC - Coordenadoria",
            "Seção de Planejamento - SPLA.CPLA - Seção",
            "Coordenadoria de Captação de Recursos - CCAR.DPLIC - Coordenadoria",
            "Departamento de Governança, Processos e Estruturas Organizacionais - DGPEO.PROPLAN - Departamento",
            "Coordenadoria de Governança e Gestão de Riscos - CGGR.DGPEO - Coordenadoria",
            "Seção de Gestão de Riscos - SGR.CGGR - Seção",
            "Coordenadoria de Processos e Estruturas Organizacionais - CPEO.DGPEO - Coordenadoria",
            "Seção de Processos Organizacionais - SPORG.CPEO - Seção",

            // Pró-Reitoria de Gestão de Pessoas
            "Pró-Reitoria de Gestão de Pessoas - PROGEPE.REIT - Pró-Reitoria",
            "Secretaria da Pró-Reitoria de Gestão de Pessoas - SEC.PROGEPE - Secretaria",
            "Assessoria de Legislação de Pessoal - ALP.PROGEPE - Assessoria",
            "Departamento de Administração de Pessoas - DAP.PROGEPE - Departamento",
            "Coordenadoria de Acompanhamento e Movimentação de Pessoas - CAMP.DAP - Coordenadoria",
            "Seção de Acompanhamento e Movimentação de Pessoas - SAMP.CAMP - Seção",
            "Seção de Aposentadorias e Pensões - SAP.CAMP - Seção",
            "Coordenadoria de Cadastro e Pagamento - CCP.DAP - Coordenadoria",
            "Seção de Controle de Cadastro e Pagamento - SCCP.CCP - Seção",
            "Seção de Registro Funcional - SRF.CCP - Seção",
            "Departamento de Desempenho e Desenvolvimento de Pessoas - DDP.PROGEPE - Departamento",
            "Coordenadoria de Concursos e Desenvolvimento de Pessoas - CCDP.DDP - Coordenadoria",
            "Seção de Avaliação de Desempenho - SAD.CCDP - Seção",
            "Seção de Capacitação e Desenvolvimento - SCD.CCDP - Seção",
            "Departamento de Qualidade de Vida - DQV.PROGEPE - Departamento",
            "Seção de Apoio Administrativo - SAAD.DQV - Seção",
            "Seção Médica e Assistência à Saúde - SMAS.DQV - Seção",
            "Seção de Segurança do Trabalho e Saúde Ocupacional - STSO.DQV - Seção",
            "Seção de Qualidade de Vida e Responsabilidade Social - SQRS.DQV - Seção",
            "Seção de Assistência Psicossocial - SAPS.DQV - Seção",

            // Pró-Reitoria de Assistência Estudantil
            "Pró-Reitoria de Assistência Estudantil - PRAE.REIT - Pró-Reitoria",
            "Secretaria da Pró-Reitoria de Assistência Estudantil - SEC.PRAE - Secretaria",
            "Departamento de Gestão Financeira e Restaurante Universitário - DGFIR.PRAE - Departamento",
            "Coordenadoria de Apoio Administrativo e Financeiro - CAAF.DGFIR - Coordenadoria",
            "Seção de Pagamentos - SPA.CAAF - Seção",
            "Seção de Gerenciamento de Dados e Informação - SGIDL.CAAF - Seção",
            "Coordenadoria de Gestão Alimentar e Restaurante Universitário - CGAR.DGFIR - Coordenadoria",
            "Departamento de Políticas Estudantis e Ações Afirmativas - DPEA.PRAE - Departamento",
            "Coordenadoria de Atenção e Permanência Estudantil - CAPE.DPEA - Coordenadoria",
            "Seção de Ingresso e Acompanhamento - SIA.CAPE - Seção",
            "Seção de Promoção de Arte e Cultura e Diversidades - SPAD.CAPE - Seção",
            "Seção de Promoção da Saúde, Esporte e Lazer - SPEL.CAPE - Seção",
            "Coordenadoria de Gestão de Moradias Estudantis - CGME.DPEA - Coordenadoria",

            // Pró-Reitoria de Ensino e Graduação
            "Pró-Reitoria de Ensino e Graduação - PREG.REIT - Pró-Reitoria",
            "Secretaria da Pró-Reitoria de Ensino e Graduação - SEC.PREG - Secretaria",
            "Departamento de Práticas de Formação Inicial e Continuada - DPFIC.PREG - Departamento",
            "Coordenadoria de Estágio - CES.DPFIC - Coordenadoria",
            "Coordenadoria de Programas Acadêmicos - CPAC.DPFIC - Coordenadoria",
            "Seção de Apoio Administrativo - SAA.CPAC - Seção",
            "Coordenadoria de Aperfeiçoamento Docente - CAD.DPFIC - Coordenadoria",
            "Departamento de Acompanhamento Educacional - DAE.PREG - Departamento",
            "Coordenadoria de Orientação Pedagógica - COP.DAE - Coordenadoria",
            "Seção de Apoio Administrativo - SAA.COP - Seção",
            "Coordenadoria de Monitoramento de Egressos - CME.DAE - Coordenadoria",
            "Departamento de Ensino - DENS.PREG - Departamento",
            "Coordenadoria de Regulação dos Cursos - CRC.DENS - Coordenadoria",
            "Seção de Apoio Administrativo - SAA.CRC - Seção",
            "Coordenadoria de Planejamento de Ensino - CPLE.DENS - Coordenadoria",
            "Seção de Apoio Administrativo - SAA.CPLE - Seção",

            // Pró-Reitoria de Pesquisa, Pós-Graduação e Inovação
            "Pró-Reitoria de Pesquisa, Pós-Graduação e Inovação - PRPPGI.REIT - Pró-Reitoria",
            "Secretaria da Pró-Reitoria de Pesquisa, Pós-Graduação e Inovação - SEC.PRPPGI - Secretaria",
            "Departamento de Pesquisa - DPESQ.PRPPGI - Departamento",
            "Coordenadoria de Iniciação Científica - CIC.DPESQ - Coordenadoria",
            "Seção de Divulgação Científica - SDC.CIC - Seção",
            "Coordenadoria de Coleções, Herbário e Biotério - CCHB.DPESQ - Coordenadoria",
            "Coordenadoria de Ética na Pesquisa - CEPESQ.DPESQ - Coordenadoria",
            "Departamento de Pós-Graduação - DPG.PRPPGI - Departamento",
            "Coordenadoria de Programas Stricto Sensu - CPSS.DPG - Coordenadoria",
            "Coordenadoria de Programas Lato Sensu e Ensino à Distância - CPLSE.DPG - Coordenadoria",
            "Seção de Residência - SRES.CPLSE - Seção",
            "Coordenação do Programa de Mestrado Profissional em Letras - CPROFLETRAS.DPG - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CPROFLETRAS - Seção",
            "Coordenação do Programa de Pós-Graduação em Ciência Animal e Pastagens - CPPGCAP.DPG - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CPPGCAP - Seção",
            "Coordenação do Programa de Pós-Graduação em Ciências Ambientais - CPPCIAM.DPG - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CPPCIAM - Seção",
            "Coordenação do Programa de Pós-Graduação em Produção Agrícola - CPPGPA.DPG - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CPPGPA - Seção",
            "Coordenação do Programa de Pós-Graduação em Sanidade e Reprodução de Animais de Produção - CPPGSRAP.DPG - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CPPGSRAP - Seção",
            "Coordenação do Programa de Pós-Graduação em Ciência da Computação - CPPGCC.DPG - Coordenadoria",
            "Departamento de Inovação Tecnológica e Empreendedorismo - DITE.PRPPGI - Departamento",
            "Seção de Propriedade Intelectual - SPI.DITE - Seção",
            "Seção de Capacitação, Difusão e Transferência de Tecnologias - SCDT.DITE - Seção",
            "Seção de Programas de Empreendedorismo e Inovação - SPEI.DITE - Seção",
            "Seção de Startups e Incubação - SSI.DITE - Seção",

            // Pró-Reitoria de Extensão e Cultura
            "Pró-Reitoria de Extensão e Cultura - PREC.REIT - Pró-Reitoria",
            "Secretaria da Pró-Reitoria de Extensão e Cultura - SEC.PREC - Secretaria",
            "Departamento de Planejamento, Captação e Gerenciamento de Recursos - DPCGR.PREC - Departamento",
            "Coordenadoria de Planejamento e Captação de Recursos - CPCR.DPCGR - Coordenadoria",
            "Seção de Integração Público-Privado - SIP.CPCR - Seção",
            "Seção de Editais e Apoio a Projetos e Programas - SEAP.CPCR - Seção",
            "Coordenadoria de Gerenciamento de Recursos - CGR.DPCGR - Coordenadoria",
            "Seção de Prestação de Contas, Estatística e Arquivo - SPCEA.CGR - Seção",
            "Departamento de Difusão Científica, Tecnológica e Inovação - DDCTI.PREC - Departamento",
            "Seção de Eventos - SEVEN.DDCTI - Seção",
            "Seção de Educação Continuada - SECON.DDCTI - Seção",
            "Seção de Certificação - SCER.DDCTI - Seção",
            "Departamento de Arte, Cultura e Assuntos Comunitários - DACA.PREC - Departamento",
            "Coordenadoria de Arte e Cultura - CAC.DACA - Coordenadoria",
            "Seção de Memória e Patrimônio da Universidade - SMPU.CAC - Seção",
            "Seção de Produção Artística e Cultural - SPAC.CAC - Seção",
            "Coordenadoria de Assuntos Comunitários - CASC.DACA - Coordenadoria",
            "Seção de Incubadora de Organizações Sociais e Coletivas - SIOC.CASC - Seção",

            // Departamento de Laboratórios Multiusuários
            "Departamento de Laboratórios Multiusuários - DLM.REIT - Departamento",
            "Coordenadoria de Apoio Administrativo - CAA.DLM - Coordenadoria",
            "Seção de Laboratórios de Apoio à Pesquisa - SLAP.CAA - Seção",
            "Seção de Laboratórios de Ensino - SLE.CAA - Seção",
            "Seção de Laboratório de Anatomia e Patologia Animal - SLAPA.CAA - Seção",
            "Seção de Laboratório de Ciência e Tecnologia de Alimentos - SLCTAL.CAA - Seção",
            "Seção de Laboratórios de Pós-Graduação - SLP.CAA - Seção",

            // Departamento de Registro e Controle Acadêmico
            "Departamento de Registro e Controle Acadêmico - DRCA.REIT - Departamento",
            "Secretaria do Departamento de Registro e Controle Acadêmico - SEC.DRCA - Secretaria",
            "Coordenadoria de Registro e Expedição de Documentos - CRED.DRCA - Coordenadoria",
            "Seção de Controle e Documentação - SCD.CRED - Seção",
            "Seção de Registro e Expedição de Diploma - SRED.CRED - Seção",
            "Coordenadoria de Graduação - CGRAD.DRCA - Coordenadoria",
            "Seção de Admissão e Cadastro de Graduação - SACG.CGRAD - Seção",
            "Seção de Movimentação e Controle Acadêmica de Graduação - SMCAG.CGRAD - Seção",
            "Coordenadoria de Pós-Graduação - CPGRAD.DRCA - Coordenadoria",
            "Seção de Admissão e Cadastro de Pós-Graduação - SACPG.CPGRAD - Seção",
            "Seção de Controle de Pós-Graduação - SCPG.CPGRAD - Seção",
            "Coordenadoria de Educação à Distância - CED.DRCA - Coordenadoria",
            "Seção de Admissão e Cadastro de Educação à Distância - SACED.CED - Seção",
            "Seção de Movimentação e Controle de Ensino à Distância - SMCED.CED - Seção",

            // Sistema de Tecnologia da Informação
            "Sistema de Tecnologia da Informação - STI.REIT - Sistema",
            "Secretaria do Sistema de Tecnologia da Informação - SEC.STI - Secretaria",
            "Coordenadoria de Governança de Tecnologia da Informação - CGTI.STI - Coordenadoria",
            "Coordenadoria de Redes e Comunicação - CRC.STI - Coordenadoria",
            "Coordenadoria de Serviços Computacionais - CSC.STI - Coordenadoria",
            "Coordenadoria de Sistemas da Informação - CSI.STI - Coordenadoria",
            "Seção Multidisciplinar de Tecnologias Sociais - SMTS.CSI - Seção",
            "Seção Multidisciplinar de Tecnologias Institucionais - SMTI.CSI - Seção",

            // Arquivo Geral
            "Arquivo Geral - ARG.REIT - Arquivo Geral",
            "Seção de Gestão de Arquivos Setoriais - SGAS.ARG - Seção",
            "Seção de Processamento Técnico e Pesquisas - SPTP.ARG - Seção",

            // Campus Universitário Sede
            "Campus Universitário Sede - CAMUS.REIT - Campus",
            "Diretoria de Centro I - DCI.CAMUS - Diretoria",
            "Secretaria do Campus Universitário Sede - SEC.CAMUS - Secretaria",
            "Coordenação do Curso de Bacharelado em Agronomia - CCBAG.CAMUS - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CCBAG - Seção",
            "Coordenação do Curso de Bacharelado em Ciência da Computação - CCBCC.CAMUS - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CCBCC - Seção",
            "Coordenação do Curso de Bacharelado em Engenharia de Alimentos - CCBEL.CAMUS - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CCBEL - Seção",
            "Coordenação do Curso de Bacharelado em Medicina Veterinária - CCBMV.CAMUS - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CCBMV - Seção",
            "Coordenação do Curso de Bacharelado em Zootecnia - CCBZO.CAMUS - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CCBZO - Seção",
            "Coordenação do Curso de Licenciatura em Pedagogia - CCLPE.CAMUS - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CCLPE - Seção",
            "Coordenação do Curso de Licenciatura em Letras - CCLLE.CAMUS - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CCLLE - Seção",
            "Coordenação do Curso de Bacharelado em Administração - CCBAD.CAMUS - Coordenadoria",
            "Coordenação dos Cursos de Educação à Distância - CCEAD.CAMUS - Coordenadoria",
            "Seção de Apoio Administrativo e Acadêmico - SAAA.CCEAD - Seção",

            // Campi Universitário Externo A
            "Campi Universitário Externo A - CAMUEA.REIT - Campi",
            "Secretaria do Campi Universitário Externo A - SEC.CAMUEA - Secretaria",
            "Diretoria de Centro I - DCI.CAMUEA - Diretoria",
            "Diretoria de Centro II - DCII.CAMUEA - Diretoria",
            "Diretoria de Centro III - DCIII.CAMUEA - Diretoria",
            "Seção de Administração de Pessoal - SAP.CAMUEA - Seção",
            "Seção de Almoxarifado e Patrimônio - SALP.CAMUEA - Seção",
            "Seção de Apoio Acadêmico - SAA.CAMUEA - Seção",
            "Seção de Biblioteca - SBI.CAMUEA - Seção",
            "Seção de Compras e Licitações - SCL.CAMUEA - Seção",
            "Seção de Contabilidade - SCON.CAMUEA - Seção",
            "Seção de Estágio e Programas Acadêmicos - SEPA.CAMUEA - Seção",
            "Seção de Tecnologia da Informação - STI.CAMUEA - Seção",

            // Campi Universitário Externo B
            "Campi Universitário Externo B - CAMUEB.REIT - Campi",
            "Secretaria do Campi Universitário Externo B - SEC.CAMUEB - Secretaria",
            "Diretoria de Centro I - DCI.CAMUEB - Diretoria",
            "Diretoria de Centro II - DCII.CAMUEB - Diretoria",
            "Diretoria de Centro III - DCIII.CAMUEB - Diretoria",
            "Seção de Administração de Pessoal - SAP.CAMUEB - Seção",
            "Seção de Almoxarifado e Patrimônio - SALP.CAMUEB - Seção",
            "Seção de Apoio Acadêmico - SAA.CAMUEB - Seção",
            "Seção de Biblioteca - SBI.CAMUEB - Seção",
            "Seção de Compras e Licitação - SCL.CAMUEB - Seção",
            "Seção de Contabilidade - SCON.CAMUEB - Seção",
            "Seção de Estágio e Programas Acadêmicos - SEPA.CAMUEB - Seção",
            "Seção de Tecnologia da Informação - STI.CAMUEB - Seção",

            // Sistema Integrado de Bibliotecas
            "Sistema Integrado de Bibliotecas - SIB.REIT - Sistema",
            "Coordenadoria de Serviço de Referência - CSR.SIB - Coordenadoria",
            "Seção de Atendimento ao Usuário e Ação Cultural - SAUAC.CSR - Seção",
            "Seção de Acessibilidade Informacional - SAICSR - Seção",
            "Coordenadoria de Serviços Digitais - CSD.SIB - Coordenadoria",
            "Seção de Tecnologias Digitais - STD.CSD - Seção",
            "Seção de Memória Institucional - SMI.CSD - Seção",
            "Coordenadoria de Acervo e Representação da Informação - CARI.SIB - Coordenadoria",
            "Seção de Seleção e Aquisição - SSA.CARI - Seção",
            "Seção de Processamento Técnico - SPT.CARI - Seção",

            // Fazenda Universitária
            "Fazenda Universitária - FUNI.REIT - Fazenda Universitária",

            // Hospital Veterinário Universitário
            "Hospital Veterinário Universitário - HVU.REIT - Hospital Veterinário Universitário",
            "Secretaria do Hospital Veterinário Universitário - SEC.HVU - Secretaria",
            "Secretaria de Assuntos Específicos - SAE.HVU - Secretaria",
            "Coordenadoria de Pequenos Animais - CPAN.HVU - Coordenadoria",
            "Seção de Clínica Médica de Pequenos Animais - SCMPAN.CPAN - Seção",
            "Seção de Cirurgia e Anestesiologia de Pequenos Animais - SCAPAN.CPAN - Seção",
            "Coordenadoria de Grandes Animais - CGAN.HVU - Coordenadoria",
            "Seção de Clínica Médica de Grandes Animais - SCMGAN.CGAN - Seção",
            "Seção de Cirurgia e Anestesiologia de Grandes Animais - SCAGAN.CGAN - Seção",
            "Seção de Farmácia - SFAR.HVU - Seção",
            "Seção de Diagnóstico por Imagem - SDI.HVU - Seção",
            "Seção de Laboratórios de Apoio - SLA.HVU - Seção"
    );


    private final TipoUnidadeAdministrativaRepository tipoUnidadeAdministrativaRepository;


    private final UnidadeAdministrativaRepository unidadeAdministrativaRepository;

    @PostConstruct
    @Transactional
    public void init() {
        // Verificar se as tabelas já estão populadas
        if (unidadeAdministrativaRepository.count() > 0 || tipoUnidadeAdministrativaRepository.count() > 0) {
            return;
        }

        // Passo 1: Inserir tipos de unidade administrativa
        List<String> nomes = List.of(
                "Reitoria",
                "Gabinete",
                "Secretaria",
                "Assessoria",
                "Vice-Reitoria",
                "Seção",
                "Diretoria",
                "Coordenadoria",
                "Procuradoria",
                "Ouvidoria",
                "Auditoria",
                "Pró-Reitoria",
                "Departamento",
                "Prefeitura",
                "Arquivo Geral",
                "Campus",
                "Campi",
                "Instituto",
                "Sistema",
                "Fazenda Universitária",
                "Hospital Veterinário Universitário"
        );

        List<TipoUnidadeAdministrativa> tipos = nomes.stream()
                .map(nome -> {
                    TipoUnidadeAdministrativa tipo = new TipoUnidadeAdministrativa();
                    tipo.setNome(nome);
                    return tipo;
                })
                .toList();

        tipoUnidadeAdministrativaRepository.saveAll(tipos);

        // Passo 2: Processar unidades administrativas
        Map<String, UnidadeAdministrativa> unidadesSalvas = new HashMap<>();
        List<UnidadeAdministrativa> unidades = new ArrayList<>();

        for (String linha : ESTRUTURA) {
            String[] partes = linha.split(" - ");
            if (partes.length != 3) {
                System.err.println("Linha inválida: " + linha);
                continue;
            }

            String nome = partes[0].trim();
            String codigo = partes[1].trim();
            String tipo = partes[2].trim();

            UnidadeAdministrativa unidade = new UnidadeAdministrativa();
            unidade.setNome(nome);
            unidade.setCodigo(codigo);

            // Buscar tipo
            TipoUnidadeAdministrativa tipoUnidade = tipoUnidadeAdministrativaRepository.findByNome(tipo);
            if (tipoUnidade == null) {
                System.err.println("Tipo não encontrado: " + tipo);
                continue;
            }
            unidade.setTipoUnidadeAdministrativa(tipoUnidade);

            // Buscar unidade pai
            String codigoPai = extrairCodigoPai(codigo, unidadesSalvas);
            if (codigoPai != null && unidadesSalvas.containsKey(codigoPai)) {
                unidade.setUnidadePai(unidadesSalvas.get(codigoPai));
            }

            unidades.add(unidade);
            unidadesSalvas.put(codigo, unidade);
        }

        // Salvar todas as unidades
        unidadeAdministrativaRepository.saveAll(unidades);
    }

    private String extrairCodigoPai(String codigo, Map<String, UnidadeAdministrativa> unidadesSalvas) {
        if (!codigo.contains(".")) {
            return null; // Sem pai
        }

        // Encontrar o código pai removendo o prefixo até o primeiro ponto
        String[] partes = codigo.split("\\.", 2);
        if (partes.length < 2) {
            return null;
        }

        // Procurar o código pai completo no mapa
        String sufixo = partes[1];
        for (String codigoSalvo : unidadesSalvas.keySet()) {
            if (codigoSalvo.equals(sufixo) || codigoSalvo.endsWith("." + sufixo)) {
                return codigoSalvo;
            }
        }

        // Retornar o sufixo como fallback (pode precisar de ajuste para casos complexos)
        return sufixo;
    }
}