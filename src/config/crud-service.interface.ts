/**
 * Interface for consistent naming of service functions used in CRUD.
 */
export interface CrudRepository<T> {
  /**
   * Create a new resource and store in the database.
   * @param data - Data used to create a `T` type.
   */
  create(data: unknown): Promise<T>;

  /**
   * Update a resource stored in the database.
   * @param where - How to uniquely identify a resource.
   * @param data - Data used to update resource.
   */
  update(where: unknown, data: unknown): Promise<T>;

  /**
   * Delete a resource from the database. Returns true if deleted, false otherwise.
   * @param where - How to uniquely identify a resource.
   */
  delete(where: unknown): Promise<boolean>;

  /**
   * Find a single resource in the database.
   * @param where - How to uniquely identify a resource.
   */
  findOne(where: unknown): Promise<T | null>;

  /**
   * Find multiple resources, optionally attempting to narrow with a clause.
   * @param where - How to identify the group of resources.
   */
  findMany(where: unknown): Promise<Array<T>>;
}
