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
            nodes: [
                // {data: {id: "a"}},
                // {data: {id: "b"}},
                // {data: {id: 'c', name: "yeah"}},
                // {data: {id: 'd', name: "yeah"}},
                // {data: {id: 'e', name: "yeah"}}
            ],

            edges: [
                // {
                //     data: {
                //         id: 'ae',
                //         weight: 1,
                //         source: 'a',
                //         target: 'e',
                //         cars: [{pos: 0.1, id: "123"}, {pos: 0.3, id: "123"}]
                //     }
                // },
                // {
                //     data: {
                //         id: "ab",
                //         weight: 1,
                //         source: "a",
                //         target: "b"
                //     }
                // },
                // {
                //     data: {
                //         id: "ba",
                //         weight: 1,
                //         source: "b",
                //         target: "a"
                //     }
                // },
                // {data: {id: 'bc', weight: 5, source: 'b', target: 'c'}},
                // {data: {id: 'ce', weight: 6, source: 'c', target: 'e'}},
                // {data: {id: 'cd', weight: 2, source: 'c', target: 'd'}},
                // {data: {id: 'de', weight: 7, source: 'd', target: 'e'}}
            ]
        },

        layout: {
            name: 'breadthfirst',
            directed: true,
            roots: '#a',
            padding: 10
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
        if (data.edges) {
            data.edges.map(function (e) {
                var j = cy.$("#" + e.edgeId)[0];
                if (j) {
                    j.data('cars', e.carPositions);
                } else {
                    if(!cy.$("#" + e.from)[0]){
                        cy.add({group: "nodes", data: {id: e.from}});
                    }
                    if(!cy.$("#" + e.to)[0]){
                        cy.add({group: "nodes", data: {id: e.to}});
                    }
                    cy.add({group: "edges", data: {id: e.edgeId, source: e.from, target: e.to}});
                    cy.layout({
                        name: 'breadthfirst',
                        directed: true,
                        roots: '#a',
                        padding: 10
                    })
                }
            })
        }

    }


    applyCarPositions("ab", [{pos: 0.4, id: "123"}, {pos: 0.3, id: "123"}])

    var exampleSocket = new WebSocket("ws://localhost:9898");
    exampleSocket.onerror = function (e) {
        console.log(e)
    }
    exampleSocket.onopen = function () {
        console.log("OPEN")
    }
    exampleSocket.onmessage = function (m) {
        var msg = JSON.parse(m.data)
        applyCarPositions(msg)
        // console.log(msg)
    }


}); // on dom ready