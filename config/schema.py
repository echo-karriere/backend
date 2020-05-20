import graphene

import apps.event.schema
import apps.pages.schema


class Query(apps.pages.schema.Query, apps.event.schema.Query, graphene.ObjectType):
    pass


schema = graphene.Schema(query=Query)
