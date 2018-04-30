<?php

use yii\db\Migration;

/**
 * Class m180412_215529_firstmigration
 */
class m180412_215529_firstmigration extends Migration
{
    /**
     * {@inheritdoc}
     */
    public function safeUp()
    {

        $this->execute('
            create table publicacaouser(
                id int not null auto_increment,
                nome text NOT NULL,
                redesocial text,
                endereco text NOT NULL,
                contato text NOT NULL,
                email text,
                atvexercida text NOT NULL,
                categoria text NOT NULL,
                aprovado enum(\'S\',\'N\'),
                latitude double precision,
                longitude double precision,
                geo_gps geometry,
                img1 MediumText,
                img2 MediumText,
                img3 MediumText,
                img4 MediumText,
                campo1 text,
                campo2 text,
                campo3 text,
                campo4 text,
                campo5 text,
                campo6 text,
                campo7 text,
                campo8 text,
                campo9 text,
                campo10 text,
                primary key(id)
            );'
        );

        $this->execute('
            create table pesquisador(
                id int not null auto_increment,
                nome text,
                email text,
                senha text, 
                campo1 text,
                campo2 text,
                campo3 text,
                campo4 text,
                campo5 text,
                primary key(id)
            );'
        );

        $this->execute('
            create table publicacaopesq(
                id int not null auto_increment,
                nome text NOT NULL,
                redesocial text,
                endereco text NOT NULL,
                contato text NOT NULL,
                email text,
                atvexercida text NOT NULL,
                categoria text NOT NULL,
                anoinicio text,
                cnpj text,
                representacao text,
                recurso text,
                aprovado text,
                latitude double precision,
                longitude double precision,
                geo_gps geometry,
                pesquisador integer,
                primary key(id),
                foreign key (pesquisador) references pesquisador(id),
                img1 MediumText,
                img2 MediumText,
                img3 MediumText,
                img4 MediumText,
                campo1 text,
                campo2 text,
                campo3 text,
                campo4 text,
                campo5 text,
                campo6 text,
                campo7 text,
                campo8 text,
                campo9 text,
                campo10 text,
                campo11 text,
                campo12 text,
                campo13 text,
                campo14 text,
                campo15 text,
                campo16 text,
                campo17 text,
                campo18 text,
                campo19 text,
                campo20 text
                );'
        );

        $this->execute('
          create table avaliacaopubuser(
            id int not null auto_increment,
            nota integer,
            idpubpesq integer,
            primary key(id),
            foreign key (idpubpesq) references publicacaouser(id)
            );'
        );

        $this->execute('
            create table avaliacaopubpesq(
                id int not null auto_increment,
                nota integer,
                idpubpesq integer,
                primary key(id),
                foreign key (idpubpesq) references publicacaopesq(id)
            );'
        );
    }

    /**
     * {@inheritdoc}
     */
    public function safeDown()
    {
        $this->execute('drop table publicacaouser cascade;');
        $this->execute('drop table pesquisador cascade;');
        $this->execute('drop table publicacaopesq cascade;');
        $this->execute('drop table avaliacaopubuser cascade;');
        $this->execute('drop table avaliacaopubpesq CASCADE;');
        //return false;
    }

    /*
    // Use up()/down() to run migration code without a transaction.
    public function up()
    {

    }

    public function down()
    {
        echo "m180412_215529_firstmigration cannot be reverted.\n";

        return false;
    }
    */
}
