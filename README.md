En el archivo ApiClient hay que cambiar la variable "BASE_URL" por la IP del equipo donde se esta ejecutando la API, 
o en su caso, la URL de la PAI cuando se hace el deploy

Al ejecutarlo la API local tambien hay que ejecutar "stripe listen --forward-to localhost:3000/webhook-stripe" en una consola a aprte para que funcionen
los webhooks.

Hay que ejecutar en una consola a parte "ngrok http 3000" y copiar la url https para poder ejecutar el api en vscode