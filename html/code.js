$(function () { // on dom ready

    var cy = cytoscape({
        container: document.getElementById('cy'),


        boxSelectionEnabled: true,
        autounselectify: false,
        wheelSensitivity: 0.1,
        style: cytoscape.stylesheet()
            .selector('node')
            .css({
                'content': 'data(id)'
            })
            .selector('edge')
            .css({
                'target-arrow-shape': 'triangle',
                'width': 4,
                'content': 'data(id)',
                'line-color': '#ddd',
                'target-arrow-color': '#ddd',
                'curve-style': 'bezier'
            })
            .selector('.highlighted')
            .css({
                'background-color': '#61bffc',
                'line-color': '#61bffc',
                'target-arrow-color': '#61bffc',
                'transition-property': 'background-color, line-color, target-arrow-color',
                'transition-duration': '0.5s'
            }),

        elements: {
            nodes: [],
            edges: [ ]
        },

        layout: {
            name: 'preset',
            fit: true, // whether to fit to viewport
            padding: 30
        }
    });

    var bfs = cy.elements().bfs('#a', function () {
    }, true);

    // var i = 0;
//     var highlightNextEle = function(){
//         if( i < bfs.path.length ){
//             bfs.path[i].addClass('highlighted');
//
//             i++;
//             setTimeout(highlightNextEle, 1000);
//         }
//     };
//
// // kick off first highlight
//     highlightNextEle();


    var applyCarPositions = function (data) {
        var n = cy.$("#" + data.nodeId)[0];
        if(data.pos && n){
            // console.log(data.type)
            n.position("x",parseInt(data.pos.x))
            n.position("y",parseInt(data.pos.y))
            if(data.type == "GasStation()"){
                n.addClass('highlighted');
            }
        }
        if (data.edges) {
            data.edges.map(function (e) {
                var j = cy.$("#" + e.edgeId)[0];
                if (j) {
                    j.data('cars', e.carPositions);
                } else {
                    if (!cy.$("#" + e.from)[0]) {
                        cy.add({group: "nodes", data: {
                            id: e.from},
                            position: {
                                x: 100,
                                y: 100
                            }
                        });
                    }
                    if (!cy.$("#" + e.to)[0]) {
                        cy.add({group: "nodes", data: {id: e.to}});
                    }
                    cy.add({group: "edges", data: {id: e.edgeId, source: e.from, target: e.to}});
                    cy.layout({
                        name: 'preset',
                        padding: 30
                    })
                }
            })
        }

    }

    var webSocket;


    var startSocket = function () {
        var exampleSocket = new WebSocket("ws://localhost:9898");
        exampleSocket.onerror = function (e) {
            console.log(e)
            setTimeout(function () {
                startSocket()
            }, 2000)
        }
        exampleSocket.onopen = function () {
            console.log("OPEN")
        }
        exampleSocket.onmessage = function (m) {
            var msg = JSON.parse(m.data)
            applyCarPositions(msg)
            // console.log(msg)
        }
        exampleSocket.onclose = function () {
            setTimeout(function () {
                startSocket()
            }, 2000)
        }
    }
    startSocket()


}); // on dom ready