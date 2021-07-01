# Buracos da Cidade
## Requisitos

- [OpenJDK11](https://download.java.net/openjdk/jdk11/ri/openjdk-11+28_windows-x64_bin.zip)
- [PostgresSQL](https://www.enterprisedb.com/postgresql-tutorial-resources-training?cid=48) (sugiro instalar o PSQL junto)
- [DBeaver](https://dbeaver.io/files/dbeaver-ce-latest-x86_64-setup.exe) (outra sugestão boa se vc n tiver um programa pra mexer no banco e tem amor pela própria vida)

## Instalação

O openJDK11 é só instalar na pasta normal dele (c:\program_files\Java\jdk11...)

O postgres também é só seguir o next, next la mas cuidado na hora de colocar o usuario e senha, A api ta configurada pra usar senha "postgres" e usuario "postgres", então se tu colocar diferente disso vai ter que mudar nas configurações do projeto (src/main/resources/application.properties a primeira prop la de spring.datasource é oque o projeto utiliza)

O Dbeaver não tem nada de especial na instalação...

## Setup
##### Eclipse
Botão direito no projeto > build path > libraries > clica em "JRE system Library" > na lateral "Edit" > "Execution environment" e marca a opção JavaSE-11

##### Postgres
Na barra de busca do windows digite "SQL Shell (PSQL)" (só psql ja acha).
preencher os valores caso eles ja não estejam dentro das [chaves], se ja estiver é só dar um enter e ser feliz...
Server: localhost
Database: postgres
Port: 5432
Username: postgres (se tiver colocado diferente coloca ai)
Password for postgres: postgres (se tiver colocado diferente coloca ai)

As vezes aparece uns warnings, mas caso não seja nada demais não esquenta, ele deve aparecer na parte de baixo la um "postgres=#"

Basta digitar "create database buracodc;" (não esquece do ; pelo amor de Cristo Jesus, senão vc vai ficar q nem uma barata tonta tentando achar o pq o psql não funciona)(caso queira mudar o nome do banco é só criar com um nome diferente e no mesmo application.properties mudar o nome la na spring.datasource.url).
Esse comando só vai ser utilizado caso o banco não exista, isso pode ocorrer caso o banco seja dropado.
Depois disso pode fechar o psql e usar o DBeaver (ou qualquer outro sgbd) normalmente

##### Executar o projeto
Basta apertar com o botão direito no arquivo src/main/java/com/BuracosDCApi/BuracosDcApiApplication.java e "run as java application", ela é a sua classe principal do projeto.

## API

A api é dividida em basicamente 3 camadas + uma camada auxiliar de model

- Controller (são os endpoints, basicamente os interceptadores de requisição, são também os responsaveis pelas respostas que serão enviadas para o front)
- Service (regras de negocio)
- Repository (camada de acesso aos dados, basicamente onde serão escritas as sqls (que são na verdade HQL por conta do hibernate ... boa sorte amigo))
- Model (representação das entidades que serão salvas no banco)


## Implementação

Aconselho fortemente que se começe implementando os models (criei um model de exemplo bem simples só para servir de referencia ModelDeExemplo.java)

Depois crie o repository (acontece bastante dele ficar vazio por conta do genericRepository) mas atente pois ele NÃO É uma classe e sim uma interface.
Depois crie o service e por ultimo o controller,
e PRONTO, simples assim ...



Just kidding
mas é isso mesmo, só isso.

## Utilização do spring security

O spring security ta habilitado no projeto então algumas coisas são importantes:

1- Por conta do Spring security foi criado um filtro na classe PapelFilter.java onde é possivel adicionar rotas publicas na aplicação. Por padrão somente login e refresh de sessão são publicos, mas uma opção boa é implementar um metodo para retornar a hora atual do servidor back end para o front (é util em alguns casos fica a dica)

2 - Para acessar algum endpoint fora dos publicos vc obrigatoriamente precisa de um token anexado na requisição, esse token é retornado no login e tem tempo de vida de 8hrs (configurável no arquivo src/main/resources/jwt.properties em ms)

3- O sistema esta configurado em varias camadas de dados:

- Usuario (seu usuario ... login, senha e email)
- Papel (admin, usuario normal, gestor, ...)
- Rotas (serão associados ao papel, nela vc define as rotas do sistema e o acesso a elas (ex:. vc pode criar uma rota que para determinado papel só tenha acesso de leitura e não de escrita))
- Permissao (associa um usuario a um papel)

ps: as rotas não são exclusivas, ou seja
se vc tem uma rota por exemplo: "/usuario" vc pode dizer que o admin tem acesso de "CRUD" e dizer que o gestor tem acesso de "R" ou "CR" ...
ps2: sugiro depois para facilitar sua vida que seja desenvolvido alguma forma para se cadastrar usuario que não seja no UsuarioController (talvez criar um controller separado especifico para isso) pois, dependendo da aplicação, vc pode querer que o proprio usuario se cadastre em algo, e ai será necessário uma rota publica

##### token
Para anexar o token na requisição do front end ela deve seguir o seguinte formato

```
Authorization: "Bearer <suatoken>"
```

Só para esclarecer
Esse trecho vai ser um cabeçalho adicional da request, então veja como anexar um cabeçalho extra usando a tecnologia de requisição de front que vc utiliza.
O nome do campo do cabeçalho é Authorization e o valor é uma string que começa com "Bearer " (não esqueça do espaço) e a token que vc vai gerar ao logar (não precisa do <>)

##### setup
Para um teste inicial da aplicação rode as seguintes query no postgres (no dbeaver é só clicar no banco e apertar f3 para abrir o console la)

```sql
insert into public.papel(ativo ,codigo , data_criacao , data_modificacao , descricao , responsabilidade) 
values(true, 'PAPEL_TESTE' ,now() ,now() , 'papel de teste' , 2); 

insert into public.rotas (id_papel ,operacoes , rota) 
values((select id_papel from public.papel where codigo like '%PAPEL_TESTE%'), 'CRUD', '/endpoint');

INSERT INTO public.usuario
(ativo, data_criacao, data_modificacao, login, senha, email, data_expiracao_senha)
VALUES(true, now(), now(), 'teste', '$2a$12$5ElZNSfzLTNx2zTecNiefudXA.DU.s35YuvCAFvcDEQ1w8UdZ2AFS', 'email@teste.com', '2099-02-01');

insert into public.permissao (id_papel ,id_usuario ) values (1, 1);
```

ps: A senha cadastrada desse usuario é "teste".
ps2: Caso vc queira criar uma senha encriptada dessa manualmente basta ver o trecho de codigo comentado no BuracosDcApiApplication.java


## Dicas finais
Criei um pacote chamado exemplo no projeto, talvez seja uma boa ver como é feito nessa ordem: model > repository > service > controller

Tem uns trecho de codigo comentado pq fiquei com preguiça de fazer um exemplo maior, mas é só pra tu ter uma noção de como é.

Depois que vc tiver pegando direito a manha pode apagar ele, ou salvar em outro canto 
(caso vc apague esse package lembre de apagar na linha 21 do BuracosDcApiApplication.java a referencia a ele ... pode dar problema caso não remova) 
Sugiro que siga a organização la de pacotes, model pra models, repository para repositorys e assim por diante, tem um pacote utils que vc pode fazer algo de auxiliar, um pacote core que é onde a magia acontece...

Ah e outro detalhe importante, veja bem o pacote de exemplos que eu criei, principalmente as anotações (@) das classes elas são muito importantes, então copie igualzinho nas suas classes
