<?php

$params = require __DIR__ . '/params.php';
$db = require __DIR__ . '/db.php';

$config = [
    'id' => 'basic',
    'basePath' => dirname(__DIR__),
    'bootstrap' => ['log'],
    'aliases' => [
        '@bower' => '@vendor/bower-asset',
        '@npm'   => '@vendor/npm-asset',
    ],
    'components' => [
        'file'=>array(
            'class'=>'application.extensions.file.CFile',
        ),
        'mail' => [
            'class' => 'yii\swiftmailer\Mailer',
            'transport' => [
                'class' => 'Swift_SmtpTransport',
                'host' => 'mail.orbeapp.com',
                'username' => 'orbeapp@orbeapp.com',
                'password' => 'orbeapp123',
                'port' => '465',
                'encryption' => 'tls',
            ],
        ],

        'assetManager' => [
            'bundles' => [
                /*'yii\web\JqueryAsset' => [
                    'sourcePath' => null,   // do not publish the bundle
                    'js' => [
                        '//ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js',
                    ]
                ],*/
                'dosamigos\google\maps\MapAsset' => [
                    'options' => [
                        'key' => 'AIzaSyDpcX-GScOiQX3IxBAu_Drpet-YqjM8t0U',	//aqui coloca a chave do google maps
                        'language' => 'id',
                        'version' => '3.1.18',
                    ]
                ]
            ]
        ],

        'request' => [
            'parsers' => [
                'application/json' => 'yii\web\JsonParser',
            ],
            // !!! insert a secret key in the following (if it is empty) - this is required by cookie validation
            'cookieValidationKey' => 'iHD8f11UKfPxtRr-tr0ofWcyp690Ff7n',
        ],
        'cache' => [
            'class' => 'yii\caching\FileCache',
        ],
        'user' => [
            'class' => 'webvimark\modules\UserManagement\components\UserConfig',

            //Comment this if you don't want to record user logins  //comentar se nao quiser saber quem que logou, que horas, e auditoria(quem fez oq sobre alterações e exclusões)
            'on afterLogin' => function($event) {
                \webvimark\modules\UserManagement\models\UserVisitLog::newVisitor($event->identity->id);
            }
        ],
        'errorHandler' => [
            'errorAction' => 'site/error',
        ],
        'log' => [
            'traceLevel' => YII_DEBUG ? 3 : 0,
            'targets' => [
                [
                    'class' => 'yii\log\FileTarget',
                    'levels' => ['error', 'warning'],
                ],
            ],
        ],
        'db' => $db,

        'urlManager' => [
            'enablePrettyUrl' => true,
            'showScriptName' => false,
            'rules' => [
                ['class'=>'yii\rest\UrlRule','controller'=>'sendpubuser', 'pluralize'=>false],
                ['class'=>'yii\rest\UrlRule','controller'=>'sendpubpesq', 'pluralize'=>false],
                ['class'=>'yii\rest\UrlRule','controller'=>'sendpesquisador', 'pluralize'=>false],
                ['class'=>'yii\rest\UrlRule','controller'=>'sendavlpubuser', 'pluralize'=>false],
                ['class'=>'yii\rest\UrlRule','controller'=>'sendavlpubpesq', 'pluralize'=>false],
                ['class'=>'yii\rest\UrlRule','controller'=>'api/default', 'pluralize'=>false],
            ],
        ],

    ],
    'params' => $params,
    'modules'=>[
        'gridview' =>  [
            'class' => '\kartik\grid\Module'
        ],
        'api' => [
            'class' => 'app\modules\api\ApliModule',
        ],
        'user-management' => [
            'class' => 'webvimark\modules\UserManagement\UserManagementModule',

            'enableRegistration' => true,			//descomentar se as pessoas poder se alto registrar

            // Add regexp validation to passwords. Default pattern does not restrict user and can enter any set of characters.			//e aqui temos que tipo de senha pode ser aceitas, e aq e para usar expressoe regulares
            // The example below allows user to enter :
            // any set of characters
            // (?=\S{8,}): of at least length 8
            // (?=\S*[a-z]): containing at least one lowercase letter
            // (?=\S*[A-Z]): and at least one uppercase letter
            // (?=\S*[\d]): and at least one number
            // $: anchored to the end of the string

            //'passwordRegexp' => '^\S*(?=\S{8,})(?=\S*[a-z])(?=\S*[A-Z])(?=\S*[\d])\S*$',


            // Here you can set your handler to change layout for any controller or action
            // Tip: you can use this event in any module
            'on beforeAction'=>function(yii\base\ActionEvent $event) {						//logaut, dps de logo, faz o controle de acesso, EX: quando acessa um action, o yii ve se vc pode acessa o mesmo, (se o papel tem acesso).
                if ( $event->action->uniqueId == 'user-management/auth/login' )				//temos o usuario e papel, logo o usuarios pdoe ter papeis
                {											//antes de vc acessa uma action, vc pode fazer algum tratamento no dado(EX: vc precisa modifca o dado para uma certa pessoa)
                    $event->action->controller->layout = 'loginLayout.php';				//deixar esse padrao, pois ainda nao precisamos personalizar
                };											//quando vc nao faz a senha pelo banco de dados isso e importante, logo autentica no servidor.
            },
        ],
    ],
];

if (YII_ENV_DEV) {
    // configuration adjustments for 'dev' environment
    $config['bootstrap'][] = 'debug';
    $config['modules']['debug'] = [
        'class' => 'yii\debug\Module',
        // uncomment the following to add your IP if you are not connecting from localhost.
        //'allowedIPs' => ['127.0.0.1', '::1'],
    ];

    $config['bootstrap'][] = 'gii';
    $config['modules']['gii'] = [
        'class' => 'yii\gii\Module',
        // uncomment the following to add your IP if you are not connecting from localhost.
        //'allowedIPs' => ['127.0.0.1', '::1'],
    ];
}

return $config;
