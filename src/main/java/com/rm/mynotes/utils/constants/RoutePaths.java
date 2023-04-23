package com.rm.mynotes.utils.constants;

public class RoutePaths {
    public static final String LOGIN = "/api/auth/login";
    public static final String SIGNUP = "/api/auth/signup";
    public static final String GET_NOTE = "/api/note/{id}";
    public static final String GET_ALL_NOTES = "/api/notes/all";
    public static final String CREATE_NOTE = "/api/note/create";
    public static final String GET_COLLECTION = "/api/collection/{id}";
    public static final String DELETE_COLLECTION = "/api/collection/{id}";
    public static final String GET_FAVORITES = "/api/collection/favorites";
    public static final String GET_ALL_COLLECTIONS = "/api/collections/all";
    public static final String CREATE_NEW_COLLECTION = "/api/collection/new";
    public static final String ADD_NOTE_TO_COLLECTION = "/api/note/{noteId}/collection/{collectionId}";
}
