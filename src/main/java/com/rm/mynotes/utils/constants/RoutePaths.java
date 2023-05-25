package com.rm.mynotes.utils.constants;

public class RoutePaths {
    public static final String LOGIN = "/api/auth/login";
    public static final String SIGNUP = "/api/auth/signup";
    public static final String GET_NOTE = "/api/note/{id}";
    public static final String GET_ALL_NOTES = "/api/notes/all";
    public static final String CREATE_NOTE = "/api/note/create";
    public static final String RECOVER_NOTE = "/api/note/{noteId}/recover";
    public static final String GET_DELETED_NOTES = "/api/notes/deleted";
    public static final String DELETE_NOTE = "/api/note/{noteId}/delete";
    public static final String GET_FAVORITES = "/api/collection/favorites";
    public static final String GET_ALL_COLLECTIONS = "/api/collections/all";
    public static final String COLLECTION = "/api/collection/{collectionId}";
    public static final String CREATE_NEW_COLLECTION = "/api/collection/new";
    public static final String GET_PINNED_COLLECTIONS = "/api/collection/pinned";
    public static final String NOTE_TO_COLLECTION = "/api/note/{noteId}/collection/{collectionId}";
}
