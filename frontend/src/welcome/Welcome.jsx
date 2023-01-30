import React from "react";

function Welcome(props) {
    return (
        <div>
            <h1>{`welcome page ${props.name}`}</h1>
            <h1>{`welcome page ${props.numOfPage}`}</h1>
        </div>
    );
}

export default Welcome;