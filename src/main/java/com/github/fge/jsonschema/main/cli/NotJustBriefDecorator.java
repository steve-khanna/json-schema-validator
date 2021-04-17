package com.github.fge.jsonschema.main.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.processors.syntax.SyntaxValidator;

import java.io.IOException;

public class NotJustBriefDecorator extends AbstractReporter {
    enum Reporters implements Reporter {
        NOT_JUST_BRIEF {
            @Override
            public RetCode validateSchema(SyntaxValidator validator, String fileName, JsonNode node)
                    throws IOException {
                return AbstractReporter.Reporters.BRIEF.validateSchema(validator, fileName, node);
            }

            @Override
            public RetCode validateInstance(JsonSchema schema, String fileName, JsonNode node)
                    throws IOException, ProcessingException {
                return AbstractReporter.Reporters.QUIET.validateInstance(schema, fileName, node);
            }
        }
    }
}






















