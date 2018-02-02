import * as errorHandler from "errorhandler";
import * as express from "express";
import * as http from "http";
import * as path from "path";

const app = express();
app.use("/node_modules", express.static(path.join(__dirname, "../../../browser/node_modules")));
app.use("/js", express.static(path.join(__dirname, "../../../browser/build/js")));
app.use("/ts", express.static(path.join(__dirname, "../../../browser/build/ts")));
app.set("port", process.env.PORT || "80");
app.get("/",
    (req, res, next) => {
        return res.sendFile(path.join(__dirname, "../../../browser/html", "index.html"));
    },
);
// error handling middleware should be loaded after the loading the routes
if (app.get("env") === "development") {
    app.use(errorHandler());
}
const server = http.createServer(app);
server.listen(app.get("port"), () => {
    console.log("Server listening on port " + app.get("port") + " (http://localhost:" + app.get("port") + ")");
});
