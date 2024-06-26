import { APP_BASE_HREF } from "@angular/common";
import { CommonEngine } from "@angular/ssr";
import express, { Express } from "express";
import { fileURLToPath } from "node:url";
import { dirname, join, resolve } from "node:path";
import bootstrap from "./src/main.server";

// The Express app is exported so that it can be used by serverless Functions.
export function app(): express.Express {
    const server: Express = express();
    const serverDistFolder: string = dirname(fileURLToPath(import.meta.url));
    const browserDistFolder: string = resolve(serverDistFolder, "../browser");
    const indexHtml: string = join(serverDistFolder, "index.server.html");

    const commonEngine: CommonEngine = new CommonEngine();

    server.set("view engine", "html");
    server.set("views", browserDistFolder);

    // Example Express Rest API endpoints
    // server.get('/api/**', (req, res) => { });
    // Serve static files from /browser
    server.get(
        "**",
        express.static(browserDistFolder, {
            maxAge: "1y",
            index: "index.html"
        })
    );

    // All regular routes use the Angular engine
    server.get("**", (req, res, next) => {
        const { protocol, originalUrl, baseUrl, headers } = req;

        commonEngine
            .render({
                bootstrap,
                documentFilePath: indexHtml,
                url: `${protocol}://${headers.host}${originalUrl}`,
                publicPath: browserDistFolder,
                providers: [{ provide: APP_BASE_HREF, useValue: baseUrl }]
            })
            .then((html: string) => res.send(html))
            .catch((err: any) => next(err));
    });

    return server;
}

function run(): void {
    const port: string | number = process.env["PORT"] ?? 4000;

    // Start up the Node server
    const server: Express = app();
    server.listen(port, (): void => {
        console.log(`Node Express server listening on http://localhost:${port}`);
    });
}

run();
