# Deploy do EdenCalc

Este projeto esta preparado para usar Supabase como banco PostgreSQL e publicar a aplicacao Spring Boot em uma hospedagem Java/Docker. O dominio `edencalc.app` fica no Name.com como DNS/registrador.

## 1. Supabase

1. Crie um projeto no Supabase.
2. Abra **Project Settings > Integrations**.
3. Em **GitHub Integration**, clique em **Authorize GitHub**.
4. Escolha o repositorio `froliveirajr/projetos3`.
5. Em **Working directory**, use `.` porque a pasta `supabase/` esta na raiz do repositorio.
6. Habilite a integracao. As migrations em `supabase/migrations/` serao usadas para criar/atualizar o schema.
7. Abra **Project Settings > Database > Connection string**.
8. Copie a connection string no formato **JDBC**. Ela deve ficar parecida com:

```text
jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres?sslmode=require
```

9. Guarde tambem o usuario, normalmente `postgres.<project-ref>`, e a senha do banco.

## 2. Variaveis de ambiente

Configure estas variaveis na hospedagem:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://.../postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=postgres.SEUPROJECTREF
SPRING_DATASOURCE_PASSWORD=SENHA_DO_SUPABASE
SPRING_DATASOURCE_DRIVER_CLASS_NAME=org.postgresql.Driver
SPRING_JPA_HIBERNATE_DDL_AUTO=validate
APP_ADMIN_CNPJ=00.000.000/0001-00
APP_ADMIN_SENHA=uma-senha-forte
APP_SEED_DEMO_DATA=false
```

Para rodar localmente sem Supabase, nenhuma variavel e obrigatoria: o projeto usa H2 em memoria por padrao.

## 3. Hospedagem

O Name.com e usado principalmente para dominio/DNS. Para hospedar esta aplicacao Spring Boot, use uma plataforma com suporte a Docker ou Java, como Render, Railway, Fly.io, VPS ou similar. Este repositorio ja inclui:

- `Dockerfile` para build e execucao da aplicacao.
- `render.yaml` para deploy na Render.
- Health check em `/actuator/health`.

Na Render:

1. Crie um **Blueprint** ou **Web Service** apontando para este repositorio.
2. Escolha ambiente **Docker**.
3. Configure as variaveis acima.
4. Depois do deploy, adicione o dominio customizado `edencalc.app`.

### Sobre Vercel

A Vercel e excelente para frontend e funcoes serverless, mas este projeto hoje e uma aplicacao Spring Boot/Thymeleaf que precisa rodar como servidor Java. Para usar Vercel sem reescrever o app, o caminho mais seguro e:

- backend Spring Boot em Render/Railway/Fly.io, usando Supabase como banco;
- Vercel apenas para um frontend separado, se no futuro o projeto for migrado para Next.js/React;
- `edencalc.app` apontando para o frontend e `api.edencalc.app` para o backend.

## 4. DNS no Name.com

Depois que a hospedagem informar o destino do dominio:

- Para dominio raiz `edencalc.app`, crie registros `A`/`AAAA` apontando para os IPs da hospedagem, se ela fornecer IPs.
- Se a hospedagem fornecer um hostname, use `CNAME` para `www.edencalc.app` apontando para esse hostname.
- Redirecione `edencalc.app` para `www.edencalc.app` na propria hospedagem, ou use o recurso de apex/custom domain da plataforma se disponivel.

Na Render, normalmente voce adiciona `edencalc.app` e `www.edencalc.app` em **Settings > Custom Domains** e copia os registros DNS indicados por ela para o painel do Name.com.

## 5. Admin

O login administrativo nao fica mais fixo no codigo. Ele usa:

```text
APP_ADMIN_CNPJ
APP_ADMIN_SENHA
```

Troque `APP_ADMIN_SENHA` antes de publicar.
