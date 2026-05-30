# Projeto Edenred Brasil - CESAR School

## Deploy e banco de dados

O projeto esta preparado para usar Supabase como banco PostgreSQL e uma hospedagem com suporte a Java/Docker. Veja o passo a passo em [DEPLOYMENT.md](DEPLOYMENT.md).

Principais arquivos:

- `.env.example`: variaveis esperadas para Supabase, admin e deploy.
- `supabase/migrations`: schema versionado para a integracao Supabase + GitHub.
- `Dockerfile`: build da aplicacao Spring Boot para hospedagens Docker.
- `render.yaml`: configuracao opcional para deploy na Render.

---

<div style="display: flex; align-items: center; gap: 10px; flex-wrap: wrap;">

<a href="https://sites.google.com/cesar.school/equipe1projetos3/home?authuser=0">
  <img src="https://img.shields.io/badge/Google%20Sites-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="Google Sites"/>
</a>

<a href="https://docs.google.com/document/d/1yZMSd_Czlgrz8i_YOXLihwQG_nLGgP7-2jKLeNaW3xA/edit?tab=t.0#heading=h.ajprb7hcx6yf">
  <img src="https://img.shields.io/badge/Histórias-483D8B?style=for-the-badge&logo=google&logoColor=white" alt="Histórias de Usuário"/>
</a>

<a href="">
  <img src="https://img.shields.io/badge/Trello-0079BF?style=for-the-badge&logo=trello&logoColor=white" alt="Trello"/>
</a>

<a href="https://www.figma.com/design/pG6K1wrGpiHmdywLgu3kWZ/Eden?node-id=0-1&t=SrXtRCToj5QA2Wqt-1">
  <img src="https://img.shields.io/badge/Figma Protótipo Baixa Fidelidade-F24E1E?style=for-the-badge&logo=figma&logoColor=white" alt="Protótipo Baixa Fidelidade"/>
</a>

<a href="https://www.figma.com/proto/pG6K1wrGpiHmdywLgu3kWZ/Eden?node-id=147-283&p=f&t=3aewtfu52WG3qSGZ-1&scaling=min-zoom&content-scaling=fixed&page-id=93%3A130">
  <img src="https://img.shields.io/badge/Figma Protótipo Alta Fidelidade-F24E1E?style=for-the-badge&logo=figma&logoColor=white" alt="Protótipo Alta Fidelidade"/>
</a>

<a href="https://www.youtube.com/watch?v=T7A8ct2o29I">
    <img src="https://img.shields.io/badge/Screencast Entrega 1-b50404?style=for-the-badge&logo=youtube&logoColor=white"/>
</a>

<a href="https://www.youtube.com/watch?v=YHZzzunshrs">
    <img src="https://img.shields.io/badge/Screencast Entrega 2-b50404?style=for-the-badge&logo=youtube&logoColor=white"/>
</a>

<a href="https://www.youtube.com/watch?v=1DBaahSdahs">
    <img src="https://img.shields.io/badge/Screencast Entrega 3-b50404?style=for-the-badge&logo=youtube&logoColor=white"/>
</a>

</div>

## Descrição do Problema

A Edenred Brasil está conduzindo uma estratégia de digitalização de suas operações, migrando parte de seus produtos de meios físicos (cartões plásticos) para soluções exclusivamente digitais. Essa transformação está alinhada às tendências globais de digitalização, eficiência operacional e sustentabilidade, além de acompanhar mudanças no comportamento dos consumidores e nas dinâmicas do mundo do trabalho.

No entanto, durante esse processo de migração, surgem questionamentos por parte dos clientes quanto à adaptação às novas soluções digitais. Parte dessa resistência está relacionada a fatores culturais, operacionais e de confiança, mas também à falta de clareza sobre os benefícios concretos da mudança, especialmente sob a ótica ambiental e de sustentabilidade. 

A Edenred enfrenta um desafio estratégico de transformação digital em que a resistência dos clientes à migração de cartões físicos para soluções digitais pode comprometer retenção e crescimento. Embora exista um potencial benefício ambiental significativo na digitalização, a ausência de mensuração clara e comparativa desses impactos dificulta a comunicação de valor. O problema central não é apenas tecnológico, mas comunicacional e estratégico, uma vez que, apesar da percepção intuitiva de que soluções digitais são mais sustentáveis, não há atualmente uma ferramenta estruturada que quantifique, de forma comparativa e objetiva, o impacto ambiental das transações físicas versus digitais dentro do contexto da Edenred.

## Objetivos

- Reduzir a dependência da logística física na distribuição de cartões.
- Diminuir o uso de plástico e papel nas operações.
- Ampliar a presença digital da empresa e modernizar os processos.
- Posicionar a Edenred como referência em soluções digitais de benefícios e pagamentos.

## Entregas

<details> 
  <summary><strong>Entrega 1 🚀</strong></summary>

  <br>

<a href="https://docs.google.com/document/d/1yZMSd_Czlgrz8i_YOXLihwQG_nLGgP7-2jKLeNaW3xA/edit?tab=t.0#heading=h.ajprb7hcx6yf">
  <img src="https://img.shields.io/badge/Histórias-483D8B?style=for-the-badge&logo=google&logoColor=white" alt="Histórias de Usuário"/>
</a>

<a href="https://www.figma.com/board/rF1kwQhJsbko2e4OFjlGBK/Organiza%C3%A7%C3%A3o?t=XBDJjoIDZttjq4k0-0">
  <img src="https://img.shields.io/badge/Organização-F24E1E?style=for-the-badge&logo=figma&logoColor=white" alt="Protótipo Baixa Fidelidade"/>
</a> 

<a href="https://www.figma.com/design/pG6K1wrGpiHmdywLgu3kWZ/Eden?node-id=0-1&t=SrXtRCToj5QA2Wqt-1">
  <img src="https://img.shields.io/badge/Figma Protótipo Baixa Fidelidade-F24E1E?style=for-the-badge&logo=figma&logoColor=white" alt="Protótipo Baixa Fidelidade"/>
</a>

<a href="https://www.youtube.com/watch?v=T7A8ct2o29I">
    <img src="https://img.shields.io/badge/Screencast Entrega 1-b50404?style=for-the-badge&logo=youtube&logoColor=white"/>
</a>

</details>

<details> 
  <summary><strong>Entrega 2 🚀</strong></summary>

  <br>

  **Status:**  
  As seguintes histórias foram implementadas:
   1. Cenário de uso dos cartões  
   2. Visualizar emissão de carbono  

  **Screencast:** 
  
<a href="https://www.youtube.com/watch?v=YHZzzunshrs">
    <img src="https://img.shields.io/badge/Screencast Entrega 2-b50404?style=for-the-badge&logo=youtube&logoColor=white"/>
</a>

  **Issues/Bugs:**

  <img width="1352" height="692" alt="image" src="https://github.com/user-attachments/assets/d9a47d49-2d05-4258-a3c6-654c23a8277b" />

As issues identificadas estão diretamente relacionadas às histórias implementadas, contribuindo principalmente para a melhoria da confiabilidade, usabilidade e robustez do sistema. De forma geral, elas tratam da validação e formatação correta dos dados inseridos, da precisão nos cálculos de emissão de carbono, da consistência na recuperação de informações e do tratamento adequado de erros. Além disso, abordam aspectos importantes de experiência do usuário, como acessibilidade das mensagens, manutenção de estado após falhas e apresentação de estados vazios, garantindo uma interação mais clara e eficiente.

  **Foto das Histórias**
  <img width="1855" height="917" alt="image" src="https://github.com/user-attachments/assets/a2991c6b-0833-4afa-9ed4-cff1493d5a12" />

  <img width="1854" height="917" alt="Captura_de_tela_2026-04-05_223913" src="https://github.com/user-attachments/assets/4f5f2639-2247-4a68-b9d8-325849c51cd8" />

  

</details>

<details> 
  <summary><strong>Entrega 3 🚀</strong></summary>

  <br>

**Status:**  
  As seguintes histórias foram implementadas:
   1. Comparar impacto entre transações físicas e digitais
   2. Traduzir impacto ambiental em equivalências

  **Screencast:** 
  
<a href="https://youtu.be/1DBaahSdahs">
    <img src="https://img.shields.io/badge/Screencast Entrega 3-b50404?style=for-the-badge&logo=youtube&logoColor=white"/>
</a>

**Issues/Bugs:**

<img width="1507" height="946" alt="Captura de tela 2026-04-27 165646" src="https://github.com/user-attachments/assets/ed4fb6af-a220-4ef0-9ddc-6c1f9c4e5928" />

As issues identificadas nesta entrega estão diretamente relacionadas às histórias implementadas, com foco na melhoria da confiabilidade e integridade dos dados no sistema. Em especial, destacam-se ajustes na validação de percentuais, garantindo que os valores inseridos respeitem limites definidos (0 a 100), agora tratados também no backend.

Além disso, as correções reduzem a dependência exclusiva do frontend para validações, prevenindo inconsistências e reforçando a robustez da aplicação. Essas melhorias contribuem para um comportamento mais seguro e previsível do sistema, além de uma experiência mais consistente para o usuário.

**Foto das Histórias**

<img width="1440" height="641" alt="Captura de Tela 2026-04-27 às 16 22 27" src="https://github.com/user-attachments/assets/344c1298-c4d6-4dc9-bd4e-35de478658bb" />

<img width="1439" height="645" alt="Captura de Tela 2026-04-27 às 16 23 09" src="https://github.com/user-attachments/assets/12f5b8f8-dbf0-4857-9da2-116ae2b0ecf8" />


</details>

<details> 
  <summary><strong>Entrega 4 🚀</strong></summary>

  <br>

</details>

---

## Equipe

<div align="center">

<table>
  <tr>
    <th>Nome</th>
    <th>Função</th>
    <th>LinkedIn</th>
    <th>Email</th>
  </tr>
      <td>Eduardo Henrique</td>
    <td>Desenvolvedor Back-end</td>
    <td><a href="https://www.linkedin.com/in/eduardohasantos/" target="_blank">LinkedIn</a></td>
    <td>ehas@cesar.school</td>
  <tr>
    <td>Fábio Henrique</td>
    <td>Desenvolvedor Front-End</td>
    <td><a href="" target="_blank">LinkedIn</a></td>
    <td>fhdlla@cesar.school</td>
  </tr>
  <tr>
    <td>Felipe Carvalho</td>
    <td>Desenvolvedor Back-End</td>
    <td><a href="https://www.linkedin.com/in/felipe-borba-de-carvalho-8b29a8366/" target="_blank">LinkedIn</a></td>
    <td>fbc2@cesar.school</td>
  </tr>
  <tr>
    <td>Francisco Oliveira</td>
    <td>Product Owner</td>
    <td><a href="https://www.linkedin.com/in/francisco-oliveira-jr-39844833/" target="_blank">LinkedIn</a></td>
    <td>froj@cesar.school</td>
  </tr>
  <tr>
    <td>Gilberto Dias</td>
    <td>Cientista de Dados</td>
    <td><a href="https://www.linkedin.com/in/gilberto-dias-9b5241222/" target="_blank">LinkedIn</a></td>
    <td>gdsn@cesar.school</td>
  </tr>
  <tr>
    <td>Lucas Chaves</td>
    <td>Scrum Master</td>
    <td><a href="https://www.linkedin.com/in/lucas-chaves-29221a171/" target="_blank">LinkedIn</a></td>
    <td>lca3@cesar.school</td>
  </tr>
  <tr>
    <td>Luiz Gustavo</td>
    <td>Desenvolvedor Front-End</td>
    <td><a href="https://www.linkedin.com/in/luiz-gustavo-878620192" target="_blank">LinkedIn</a></td>
    <td>lggs@cesar.school</td>
  </tr>
  
  <tr>
    <td>Nádia Bordoni</td>
    <td>Designer</td>
    <td><a href="https://www.linkedin.com/in/nadia-bordoni-a158a1136/" target="_blank">LinkedIn</a></td>
    <td>nfb@cesar.school</td>
  </tr>

</table>
</div>
